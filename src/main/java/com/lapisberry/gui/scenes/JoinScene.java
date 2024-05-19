package com.lapisberry.gui.scenes;

import com.lapisberry.Main;
import com.lapisberry.gui.MediaController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static com.lapisberry.gui.FontPreloader.*;

public class JoinScene extends Scene {
    public JoinScene() {
        super(new StackPane(new Container()), 1280, 720);
    }

    private static class Container extends VBox {
        private Container() {
            super();
            Title title = new Title("MaKillMai");
            InputField username = new InputField("username");
            InputField ipAddress = new InputField("ip-address");
            JoinButton joinButton = new JoinButton("Join");

            getChildren().addAll(title, username, ipAddress, joinButton);
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

    private static class InputField extends TextField {
        private InputField(String promptText) {
            super();
            setPromptText(promptText);
            setFont(Font.loadFont(Inter_ExtraLight, 40));
            setBackground(new Background(new BackgroundFill(Color.valueOf("D9D9D9"), new CornerRadii(10), null)));
            setMaxWidth(400);
            setMinHeight(80);
            setAlignment(Pos.CENTER);
        }
    }

    private static class JoinButton extends Button {
        private JoinButton(String text) {
            super(text);
            setFont(Font.loadFont(Inter_SemiBold, 40));
            setBackground(new Background(new BackgroundFill(Color.valueOf("00C2FF"), new CornerRadii(40), null)));
            setMaxWidth(274);
            setMinHeight(80);
            setAlignment(Pos.CENTER);
            setOnMouseEntered(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("00A6D1"), new CornerRadii(40), null)));
                setCursor(javafx.scene.Cursor.HAND);
            });
            setOnMouseExited(e -> setBackground(new Background(new BackgroundFill(Color.valueOf("00C2FF"), new CornerRadii(40), null))));
            setOnMousePressed(e -> setBackground(new Background(new BackgroundFill(Color.valueOf("0089A9"), new CornerRadii(40), null))));
            setOnMouseReleased(e -> setBackground(new Background(new BackgroundFill(Color.valueOf("00A6D1"), new CornerRadii(40), null))));
            setOnAction(e -> {
                Main.getPrimaryStage().setScene(new LobbyScene());
                MediaController.playMediaOnce(MediaController.buttonClickSound);
            });
        }
    }
}