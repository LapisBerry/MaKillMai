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

import java.util.concurrent.atomic.AtomicReference;

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
            CreateServerButton createServerButton = new CreateServerButton();

            getChildren().addAll(title, username, ipAddress, joinButton, createServerButton);
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
            final Background regularBackground = new Background(new BackgroundFill(Color.valueOf("00C2FF"), new CornerRadii(40), null));
            final Background hoverBackground = new Background(new BackgroundFill(Color.valueOf("00A6D1"), new CornerRadii(40), null));
            final Background pressedBackground = new Background(new BackgroundFill(Color.valueOf("0089A9"), new CornerRadii(40), null));

            setFont(Font.loadFont(Inter_SemiBold, 40));
            setBackground(regularBackground);
            setMaxWidth(274);
            setMinHeight(80);
            setAlignment(Pos.CENTER);
            setOnMouseEntered(e -> {
                setBackground(hoverBackground);
                setCursor(javafx.scene.Cursor.HAND);
            });
            setOnMouseExited(e -> setBackground(regularBackground));
            setOnMousePressed(e -> setBackground(pressedBackground));
            setOnMouseReleased(e -> setBackground(hoverBackground));
            setOnAction(e -> {
                Main.getPrimaryStage().setScene(new LobbyScene());
                MediaController.playMediaOnce(MediaController.buttonClickSound);
            });
        }
    }

    private static class CreateServerButton extends Button {
        private boolean isServerCreated = false;

        private CreateServerButton() {
            super("Create Server");
            final Background blueRegularBackground = new Background(new BackgroundFill(Color.valueOf("00C2FF"), new CornerRadii(40), null));
            final Background blueHoverBackground = new Background(new BackgroundFill(Color.valueOf("00A6D1"), new CornerRadii(40), null));
            final Background bluePressedBackground = new Background(new BackgroundFill(Color.valueOf("0089A9"), new CornerRadii(40), null));

            final Background redRegularBackground = new Background(new BackgroundFill(Color.valueOf("FF0000"), new CornerRadii(40), null));
            final Background redHoverBackground = new Background(new BackgroundFill(Color.valueOf("D10000"), new CornerRadii(40), null));
            final Background redPressedBackground = new Background(new BackgroundFill(Color.valueOf("890000"), new CornerRadii(40), null));

            AtomicReference<Background> regularBackground = new AtomicReference<>(blueRegularBackground);
            AtomicReference<Background> hoverBackground = new AtomicReference<>(blueHoverBackground);
            AtomicReference<Background> pressedBackground = new AtomicReference<>(bluePressedBackground);

            setFont(Font.loadFont(Inter_SemiBold, 18));
            setBackground(regularBackground.get());
            setMaxWidth(180);
            setMinHeight(40);
            setAlignment(Pos.CENTER);
            setOnMouseEntered(e -> {
                setBackground(hoverBackground.get());
                setCursor(javafx.scene.Cursor.HAND);
            });
            setOnMouseExited(e -> setBackground(regularBackground.get()));
            setOnMousePressed(e -> setBackground(pressedBackground.get()));
            setOnMouseReleased(e -> setBackground(hoverBackground.get()));
            setOnAction(e -> {
                if (isServerCreated) {
                    Main.closeServer();
                    setText("Create Server");
                    regularBackground.set(blueRegularBackground);
                    hoverBackground.set(blueHoverBackground);
                    pressedBackground.set(bluePressedBackground);
                } else {
                    Main.createServer();
                    setText("Close Server");
                    regularBackground.set(redRegularBackground);
                    hoverBackground.set(redHoverBackground);
                    pressedBackground.set(redPressedBackground);
                }
                isServerCreated = !isServerCreated;
            });
        }
    }
}