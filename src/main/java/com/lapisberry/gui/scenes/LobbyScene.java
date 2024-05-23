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

public class LobbyScene extends Scene {
    private static final Title title = new Title("Lobby");
    private static final PlayerPanel playerPanel = new PlayerPanel();
    private static final StartButton startButton = new StartButton("Start Game");
    private static final Container container = new Container(title, playerPanel, startButton);

    public LobbyScene() {
        super(new StackPane(container), Main.getPrimaryStage().getScene().getWidth(), Main.getPrimaryStage().getScene().getHeight());
    }

    public static void updatePlayerList(LobbyController lobbyController) {
        Platform.runLater(() -> {
            PlayerPanel.PlayerList.vbox.getChildren().clear();
            lobbyController.getPlayers().forEach(pair -> addPlayer(pair.getValue()));
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
                vbox.setBackground(new Background(new BackgroundFill(Color.valueOf("D9D9D9"), new CornerRadii(0), null)));
                vbox.setAlignment(Pos.TOP_CENTER);
                vbox.setSpacing(8);
                setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                setMaxWidth(338);
                setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), null)));
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
        }
    }
}