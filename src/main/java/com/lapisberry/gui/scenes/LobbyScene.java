package com.lapisberry.gui.scenes;

import com.lapisberry.Main;
import com.lapisberry.game.controllers.LobbyController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static com.lapisberry.gui.FontPreloader.Inter_Black;
import static com.lapisberry.gui.FontPreloader.Inter_SemiBold;
import static com.lapisberry.utils.RoleCountHelper.*;

public class LobbyScene extends Scene {
    private static final Title title = new Title("Lobby");
    private static final PlayerPanel playerPanel = new PlayerPanel();
    private static final StartButton startButton = new StartButton("Start Game");
    private static final Container container = new Container(title, playerPanel, startButton);
    private static final RoleCounterContainer roleCounterContainer = new RoleCounterContainer();

    public LobbyScene() {
        super(new StackPane(roleCounterContainer, container), Main.getPrimaryStage().getScene().getWidth(), Main.getPrimaryStage().getScene().getHeight());
    }

    public static void updatePlayerList(LobbyController lobbyController) {
        Platform.runLater(() -> {
            // Update PlayerList
            PlayerPanel.PlayerList.vbox.getChildren().clear();
            lobbyController.getPlayers().forEach(pair -> addPlayer(pair.getValue()));

            // Update RoleCounter
            final int playerCount = lobbyController.getPlayers().size();
            final int emperors = getEmperors(playerCount);
            final int royalists = getRoyalists(playerCount);
            final int rebels = getRebels(playerCount);
            final int spies = getSpies(playerCount);
            RoleCounterContainer.RoleBox.updateCounter(emperors, royalists, rebels, spies);

            // Update StartButton
            if (Main.getClient().getClientId() != lobbyController.getPlayers().getFirst().getKey())
                startButton.setDisable(true);
            else
                startButton.setDisable(lobbyController.getPlayers().size() < 4 || lobbyController.getPlayers().size() > 8);
        });
    }

    private static void addPlayer(String name) {
        PlayerPanel.PlayerList.addPlayer(name);
    }

    private static class Container extends VBox {
        private Container(Node... children) {
            super(children);
            setMaxWidth(400);
            setMaxHeight(200);
            setAlignment(Pos.CENTER);
            setSpacing(17);
        }
    }

    private static class RoleCounterContainer extends BorderPane {
        private RoleCounterContainer() {
            super();
            setRight(new RoleBox());
            setAlignment(getRight(), Pos.CENTER_RIGHT);
            setPadding(new Insets(20, 20, 20, 20));
        }

        private static class RoleBox extends VBox {
            private static final RoleText emperorCounter = new RoleText("Emperor: 0");
            private static final RoleText royalistCounter = new RoleText("Royalist: 0");
            private static final RoleText rebelCounter = new RoleText("Rebel: 0");
            private static final RoleText spyCounter = new RoleText("Spy: 0");

            private RoleBox() {
                super();
                getChildren().addAll(new SubTitle("Roles"), emperorCounter, royalistCounter, rebelCounter, spyCounter);
                setMaxHeight(0);
                setAlignment(Pos.CENTER);
                setBackground(new Background(new BackgroundFill(Color.valueOf("D9D9D9"), new CornerRadii(20), null)));
                setPadding(new Insets(15, 20, 15, 20));
                setSpacing(10);
            }

            private static void updateCounter(int emperor, int royalist, int rebel, int spy) {
                Platform.runLater(() -> {
                    emperorCounter.setText("Emperor: " + emperor);
                    royalistCounter.setText("Royalist: " + royalist);
                    rebelCounter.setText("Rebel: " + rebel);
                    spyCounter.setText("Spy: " + spy);
                });
            }

            private static class SubTitle extends Text {
                private SubTitle(String text) {
                    super(text);
                    setFont(Font.loadFont(Inter_SemiBold, 36));
                    setTextAlignment(TextAlignment.CENTER);
                }
            }

            private static class RoleText extends Text {
                private RoleText(String text) {
                    super(text);
                    setFont(Font.loadFont(Inter_SemiBold, 20));
                    setTextAlignment(TextAlignment.CENTER);
                }
            }
        }
    }

    private static class Title extends Text {
        private Title(String text) {
            super(text);
            setFont(Font.loadFont(Inter_Black, 128));
            setTextAlignment(TextAlignment.CENTER);
        }
    }

    private static class PlayerPanel extends VBox {
        private PlayerPanel() {
            super();
            SubTitle subTitle = new SubTitle("Players in this lobby");
            PlayerList playerList = new PlayerList();
            setAlignment(Pos.TOP_CENTER);
            setBackground(new Background(new BackgroundFill(Color.valueOf("D9D9D9"), new CornerRadii(20), null)));
            setMinWidth(420);
            setMinHeight(450);
            setPadding(new Insets(15, 20, 15, 20));
            setSpacing(20);
            getChildren().addAll(subTitle, playerList);
        }

        private static class SubTitle extends Text {
            private SubTitle(String text) {
                super(text);
                setFont(Font.loadFont(Inter_SemiBold, 36));
                setTextAlignment(TextAlignment.CENTER);
            }
        }

        private static class PlayerList extends ScrollPane {
            private static final VBox vbox = new VBox();

            private PlayerList() {
                super();
                vbox.setMinWidth(336);
                vbox.setBackground(Background.fill(Color.valueOf("D9D9D9")));
                vbox.setAlignment(Pos.TOP_CENTER);
                vbox.setSpacing(8);
                setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                setMaxWidth(338);
                setBackground(Background.fill(Color.TRANSPARENT));
                setContent(vbox);
            }

            private static void addPlayer(String name) {
                vbox.getChildren().add(new PlayerItem(name));
            }
        }

        private static class PlayerItem extends Label {
            private PlayerItem(String text) {
                super(text);
                setBackground(new Background(new BackgroundFill(Color.valueOf("32CEFF"), new CornerRadii(10), null)));
                setFont(Font.loadFont(Inter_SemiBold, 20));
                setAlignment(Pos.CENTER);
                setMinWidth(300);
                setMinHeight(55);
            }
        }
    }

    private static class StartButton extends Button {
        private StartButton(String text) {
            super(text);
            setFont(Font.loadFont(Inter_SemiBold, 40));
            setBackground(new Background(new BackgroundFill(Color.valueOf("44FF02"), new CornerRadii(40), null)));
            setMaxWidth(274);
            setMinHeight(80);
            setAlignment(Pos.CENTER);
            setOnMouseEntered(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("3cbd0f"), new CornerRadii(40), null)));
                setCursor(javafx.scene.Cursor.HAND);
            });
            setOnMouseExited(e -> setBackground(new Background(new BackgroundFill(Color.valueOf("44FF02"), new CornerRadii(40), null))));
            setOnMousePressed(e -> setBackground(new Background(new BackgroundFill(Color.valueOf("2b850c"), new CornerRadii(40), null))));
            setOnMouseReleased(e -> setBackground(new Background(new BackgroundFill(Color.valueOf("44FF02"), new CornerRadii(40), null))));
            setOnAction(e -> new Thread(() -> {
                Platform.runLater(() -> setDisable(true));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                Platform.runLater(() -> setDisable(false));
            }, "temporary disable start button thread").start());
        }
    }
}