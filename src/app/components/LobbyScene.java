package app.components;

import app.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
            setFont(Font.loadFont(ClassLoader.getSystemResource("fonts/Inter-Black.ttf").toString(), 128));
            setTextAlignment(TextAlignment.CENTER);
        }
    }

    private static class PlayerPanel extends HBox {
        private PlayerPanel() {
            super();
            SubTitle subTitle = new SubTitle("Players in this lobby");

            setAlignment(Pos.CENTER);
            getChildren().add(subTitle);
        }

        private static class SubTitle extends Text {
            private SubTitle(String text) {
                super(text);
                setFont(Font.loadFont(ClassLoader.getSystemResource("fonts/Inter-SemiBold.ttf").toString(), 36));
                setTextAlignment(TextAlignment.CENTER);
            }
        }
    }

    private static class StartButton extends Button {
        private StartButton(String text) {
            super(text);
            setFont(Font.loadFont(ClassLoader.getSystemResource("fonts/Inter-SemiBold.ttf").toString(), 40));
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
