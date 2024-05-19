package com.lapisberry.gui.scenes;

import com.lapisberry.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    public LobbyScene() {
        super(new StackPane(new Container()), Main.getPrimaryStage().getScene().getWidth(), Main.getPrimaryStage().getScene().getHeight());
    }

    private static class Container extends VBox {
        private Container() {
            super();
            Title title = new Title("Lobby");
            PlayerPanel playerPanel = new PlayerPanel();
            StartButton startButton = new StartButton("Start Game");

            //VBox vbox = new VBox();
            getChildren().addAll(title, playerPanel, startButton);
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
            private PlayerList() {
                super();
                VBox vbox = new VBox();
                vbox.getChildren().addAll(new PlayerItem("Player 1"), new PlayerItem("Player 2"), new PlayerItem("Player 1"), new PlayerItem("Player 2"), new PlayerItem("Player 1"), new PlayerItem("Player 2"), new PlayerItem("Player 1"), new PlayerItem("Player 2"));
                vbox.setMinWidth(336);
                vbox.setBackground(new Background(new BackgroundFill(Color.valueOf("D9D9D9"), new CornerRadii(0), null)));
                vbox.setAlignment(Pos.TOP_CENTER);
                vbox.setSpacing(8);
                setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                setMaxWidth(338);
                setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), null)));
                setContent(vbox);
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