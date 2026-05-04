package com.lapisberry.gui.scenes;

import com.lapisberry.Main;
import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.entities.dice.DieFace;
import com.lapisberry.game.entities.players.Role;
import com.lapisberry.net.packets.EndRollingPacket;
import com.lapisberry.net.packets.EndTurnPacket;
import com.lapisberry.net.packets.GameStatePacket;
import com.lapisberry.net.packets.ResolveDiePacket;
import com.lapisberry.net.packets.RollDicePacket;
import com.lapisberry.net.packets.ToggleDieLockPacket;
import com.lapisberry.net.packets.UsePureMagicPacket;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.lapisberry.gui.FontPreloader.Inter_Bold;
import static com.lapisberry.gui.FontPreloader.Inter_Regular;
import static com.lapisberry.gui.FontPreloader.Inter_SemiBold;

public class GameScene extends Scene {
    // Singletons reused across re-instantiations.
    private static final BorderPane root = new BorderPane();
    private static final HBox opponentsRow = new HBox(20);
    private static final HBox diceTray = new HBox(12);
    private static final Label phaseLabel = new Label();
    private static final Label rotPoolLabel = new Label();
    private static final HBox actionsBar = new HBox(12);
    private static final VBox targetPicker = new VBox(8);
    private static final HBox selfBar = new HBox(20);
    private static final Label banner = new Label();

    private static GameStatePacket lastState;
    private static Integer pendingDieIndexForTarget;

    static {
        opponentsRow.setAlignment(Pos.CENTER);
        opponentsRow.setPadding(new Insets(20, 20, 10, 20));

        diceTray.setAlignment(Pos.CENTER);
        diceTray.setPadding(new Insets(10));

        actionsBar.setAlignment(Pos.CENTER);
        actionsBar.setPadding(new Insets(10));

        targetPicker.setAlignment(Pos.CENTER);
        targetPicker.setPadding(new Insets(8));

        selfBar.setAlignment(Pos.CENTER);
        selfBar.setPadding(new Insets(15, 20, 25, 20));

        phaseLabel.setFont(Font.loadFont(Inter_Bold, 22));
        rotPoolLabel.setFont(Font.loadFont(Inter_Regular, 16));
        banner.setFont(Font.loadFont(Inter_Bold, 36));
        banner.setVisible(false);

        VBox center = new VBox(10, phaseLabel, rotPoolLabel, diceTray, actionsBar, targetPicker, banner);
        center.setAlignment(Pos.CENTER);

        root.setTop(opponentsRow);
        root.setCenter(center);
        root.setBottom(selfBar);
    }

    public GameScene() {
        super(root, Main.getPrimaryStage().getScene().getWidth(),
                Main.getPrimaryStage().getScene().getHeight());
    }

    public static void applyState(GameStatePacket state) {
        Platform.runLater(() -> {
            lastState = state;
            // If a different die now demands targeting, drop the stale picker.
            pendingDieIndexForTarget = null;
            renderOpponents(state);
            renderSelf(state);
            renderDiceTray(state);
            renderHeader(state);
            renderActions(state);
            renderTargetPicker(state);
            renderBanner(state);
        });
    }

    // Header ------------------------------------------------------------------

    private static void renderHeader(GameStatePacket state) {
        GameStatePacket.PlayerView current = findById(state, state.getCurrentTurnClientId());
        String currentName = current != null ? current.playerName : "(none)";
        String phaseText = switch (state.getPhase()) {
            case ROLLING -> "Rolling — " + currentName + " (rolls left: " + state.getRollsLeft() + ")";
            case AWAITING_PURE_MAGIC -> "Pure Magic — " + currentName + " must decide";
            case RESOLVING -> "Resolving — " + currentName;
            case TURN_OVER -> "End of turn — " + currentName;
            case GAME_OVER -> "Game Over";
            case WAITING_FOR_START -> "Waiting...";
        };
        phaseLabel.setText(phaseText);
        rotPoolLabel.setText("Rot pool remaining: " + state.getRotPool());
    }

    // Opponents row -----------------------------------------------------------

    private static void renderOpponents(GameStatePacket state) {
        opponentsRow.getChildren().clear();
        List<GameStatePacket.PlayerView> sorted = new ArrayList<>(state.getPlayers());
        sorted.sort(Comparator.comparingInt(pv -> pv.position));
        for (GameStatePacket.PlayerView pv : sorted) {
            if (pv.clientId == state.getRecipientClientId()) continue;
            opponentsRow.getChildren().add(playerCard(pv, state));
        }
    }

    private static void renderSelf(GameStatePacket state) {
        selfBar.getChildren().clear();
        GameStatePacket.PlayerView self = findById(state, state.getRecipientClientId());
        if (self != null) selfBar.getChildren().add(playerCard(self, state));
    }

    private static VBox playerCard(GameStatePacket.PlayerView pv, GameStatePacket state) {
        VBox card = new VBox(4);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setMinWidth(180);

        boolean isCurrent = pv.clientId == state.getCurrentTurnClientId();
        String bg = !pv.alive ? "555555" : isCurrent ? "FFD56B" : "D9D9D9";
        card.setBackground(new Background(new BackgroundFill(Color.valueOf(bg), new CornerRadii(12), null)));

        Label name = new Label(pv.playerName + (isCurrent ? "  ◆" : ""));
        name.setFont(Font.loadFont(Inter_Bold, 18));

        Label characterAndRole = new Label(pv.characterName +
                (pv.role != null ? "  [" + roleLabel(pv.role) + "]" : "  [???]"));
        characterAndRole.setFont(Font.loadFont(Inter_Regular, 13));

        Label hp = new Label(pv.alive ? ("HP " + pv.hp + " / " + pv.maxHp) : "DEAD");
        hp.setFont(Font.loadFont(Inter_SemiBold, 16));

        Label rot = new Label("Rot: " + pv.rotPower);
        rot.setFont(Font.loadFont(Inter_Regular, 13));

        card.getChildren().addAll(name, characterAndRole, hp, rot);
        return card;
    }

    private static String roleLabel(Role role) {
        return switch (role) {
            case EMPEROR -> "Emperor";
            case ROYALIST -> "Royalist";
            case REBEL -> "Rebel";
            case SPY -> "Spy";
        };
    }

    // Dice tray ---------------------------------------------------------------

    private static void renderDiceTray(GameStatePacket state) {
        diceTray.getChildren().clear();
        boolean isMyTurn = state.getCurrentTurnClientId() == state.getRecipientClientId();
        for (int i = 0; i < state.getDice().size(); i++) {
            GameStatePacket.DieView dv = state.getDice().get(i);
            diceTray.getChildren().add(dieButton(i, dv, state, isMyTurn));
        }
    }

    private static Button dieButton(int index, GameStatePacket.DieView dv,
                                    GameStatePacket state, boolean isMyTurn) {
        Button b = new Button(dieFaceShort(dv.face));
        b.setFont(Font.loadFont(Inter_Bold, 18));
        b.setMinSize(80, 80);

        String bg;
        if (dv.resolved) bg = "777777";
        else if (dv.locked) bg = "FFB347";
        else bg = "D9D9D9";
        b.setBackground(new Background(new BackgroundFill(Color.valueOf(bg), new CornerRadii(10), null)));

        if (isMyTurn && state.getPhase() == GameController.Phase.ROLLING && dv.unlockable && !dv.resolved) {
            b.setOnAction(e -> Main.getClient().sendPacketToServer(new ToggleDieLockPacket(index)));
        } else if (isMyTurn && state.getPhase() == GameController.Phase.RESOLVING && !dv.resolved
                && (dv.face == DieFace.ATTACK_1 || dv.face == DieFace.ATTACK_2 || dv.face == DieFace.HEALTH_POTION)) {
            b.setOnAction(e -> {
                pendingDieIndexForTarget = index;
                renderTargetPicker(state);
            });
        } else {
            b.setDisable(true);
        }
        return b;
    }

    private static String dieFaceShort(DieFace f) {
        return switch (f) {
            case ATTACK_1 -> "⚔ 1";
            case ATTACK_2 -> "⚔ 2";
            case HEALTH_POTION -> "♥ HP";
            case ROT_POWER -> "☣ Rot";
            case PURE_MAGIC -> "✦ Pure";
            case STONE_SUPPRESSOR -> "✕ Stone";
        };
    }

    // Action buttons ----------------------------------------------------------

    private static void renderActions(GameStatePacket state) {
        actionsBar.getChildren().clear();
        boolean isMyTurn = state.getCurrentTurnClientId() == state.getRecipientClientId();
        if (!isMyTurn || state.getPhase() == GameController.Phase.GAME_OVER) return;

        switch (state.getPhase()) {
            case ROLLING -> {
                if (state.getRollsLeft() > 0) {
                    actionsBar.getChildren().add(actionButton("Roll dice (" + state.getRollsLeft() + " left)",
                            () -> Main.getClient().sendPacketToServer(new RollDicePacket())));
                }
                actionsBar.getChildren().add(actionButton("End rolling",
                        () -> Main.getClient().sendPacketToServer(new EndRollingPacket())));
            }
            case AWAITING_PURE_MAGIC -> {
                actionsBar.getChildren().add(actionButton("Use Pure Magic",
                        () -> Main.getClient().sendPacketToServer(new UsePureMagicPacket(true))));
                actionsBar.getChildren().add(actionButton("Skip Pure Magic",
                        () -> Main.getClient().sendPacketToServer(new UsePureMagicPacket(false))));
            }
            case RESOLVING -> {
                Label hint = new Label("Click a die above, then pick a target.");
                hint.setFont(Font.loadFont(Inter_Regular, 14));
                actionsBar.getChildren().add(hint);
            }
            case TURN_OVER -> actionsBar.getChildren().add(actionButton("End turn",
                    () -> Main.getClient().sendPacketToServer(new EndTurnPacket())));
            default -> { /* nothing */ }
        }
    }

    private static Button actionButton(String label, Runnable onClick) {
        Button b = new Button(label);
        b.setFont(Font.loadFont(Inter_SemiBold, 18));
        b.setMinHeight(45);
        b.setMinWidth(180);
        b.setBackground(new Background(new BackgroundFill(Color.valueOf("44FF02"), new CornerRadii(20), null)));
        b.setOnMouseEntered(e -> {
            b.setBackground(new Background(new BackgroundFill(Color.valueOf("3cbd0f"), new CornerRadii(20), null)));
            b.setCursor(javafx.scene.Cursor.HAND);
        });
        b.setOnMouseExited(e -> b.setBackground(new Background(new BackgroundFill(Color.valueOf("44FF02"), new CornerRadii(20), null))));
        b.setOnAction(e -> onClick.run());
        return b;
    }

    // Target picker -----------------------------------------------------------

    private static void renderTargetPicker(GameStatePacket state) {
        targetPicker.getChildren().clear();
        if (pendingDieIndexForTarget == null) return;
        if (state.getCurrentTurnClientId() != state.getRecipientClientId()) return;
        if (state.getPhase() != GameController.Phase.RESOLVING) return;

        int dieIndex = pendingDieIndexForTarget;
        if (dieIndex < 0 || dieIndex >= state.getDice().size()) return;
        GameStatePacket.DieView dv = state.getDice().get(dieIndex);
        if (dv.resolved) return;

        List<Integer> validTargets = computeValidTargets(state, dv.face);
        if (validTargets.isEmpty()) {
            Label none = new Label("No valid target — pick another die.");
            none.setFont(Font.loadFont(Inter_Regular, 13));
            targetPicker.getChildren().add(none);
            return;
        }

        Label prompt = new Label("Pick a target for " + dieFaceShort(dv.face));
        prompt.setFont(Font.loadFont(Inter_SemiBold, 16));
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER);
        for (int targetId : validTargets) {
            GameStatePacket.PlayerView pv = findById(state, targetId);
            if (pv == null) continue;
            int finalDieIndex = dieIndex;
            Button b = new Button(pv.playerName);
            b.setFont(Font.loadFont(Inter_Regular, 14));
            b.setOnAction(e -> {
                pendingDieIndexForTarget = null;
                Main.getClient().sendPacketToServer(new ResolveDiePacket(finalDieIndex, targetId));
            });
            row.getChildren().add(b);
        }
        Button cancel = new Button("Cancel");
        cancel.setFont(Font.loadFont(Inter_Regular, 14));
        cancel.setOnAction(e -> {
            pendingDieIndexForTarget = null;
            renderTargetPicker(state);
        });
        row.getChildren().add(cancel);
        targetPicker.getChildren().addAll(prompt, row);
    }

    private static List<Integer> computeValidTargets(GameStatePacket state, DieFace face) {
        if (face == DieFace.HEALTH_POTION) {
            List<Integer> ids = new ArrayList<>();
            for (GameStatePacket.PlayerView pv : state.getPlayers()) {
                if (pv.alive) ids.add(pv.clientId);
            }
            return ids;
        }
        int distance = (face == DieFace.ATTACK_1) ? 1 : 2;
        List<GameStatePacket.PlayerView> alive = new ArrayList<>();
        for (GameStatePacket.PlayerView pv : state.getPlayers()) if (pv.alive) alive.add(pv);
        alive.sort(Comparator.comparingInt(pv -> pv.position));
        int n = alive.size();
        if (n <= distance) return List.of();
        int curIdx = -1;
        for (int i = 0; i < alive.size(); i++) {
            if (alive.get(i).clientId == state.getCurrentTurnClientId()) { curIdx = i; break; }
        }
        if (curIdx < 0) return List.of();
        Set<Integer> ids = new HashSet<>();
        ids.add(alive.get((curIdx + distance) % n).clientId);
        ids.add(alive.get(((curIdx - distance) % n + n) % n).clientId);
        return new ArrayList<>(ids);
    }

    // Banner ------------------------------------------------------------------

    private static void renderBanner(GameStatePacket state) {
        if (state.getPhase() != GameController.Phase.GAME_OVER || state.getWinningRole() == null) {
            banner.setVisible(false);
            return;
        }
        banner.setText(roleLabel(state.getWinningRole()) + " team wins!");
        banner.setVisible(true);
    }

    // Helpers -----------------------------------------------------------------

    private static GameStatePacket.PlayerView findById(GameStatePacket state, int clientId) {
        for (GameStatePacket.PlayerView pv : state.getPlayers()) {
            if (pv.clientId == clientId) return pv;
        }
        return null;
    }
}
