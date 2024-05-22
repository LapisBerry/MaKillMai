package com.lapisberry.gui.scenes;

import com.lapisberry.Main;
import com.lapisberry.gui.MediaController;
import com.lapisberry.utils.exceptions.ConnectionRefusedException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.lapisberry.gui.FontPreloader.*;

public class JoinScene extends Scene {
    private static final Title title = new Title("MaKillMai");
    private static final InputField username = new InputField("username");
    private static final InputField ipAddress = new InputField("ip-address");
    private static final JoinButton joinButton = new JoinButton("Join");
    private static final CreateServerButton createServerButton = new CreateServerButton();
    private static final Container container = new Container(title, username, ipAddress, joinButton, createServerButton);

    private static final AlertContainer alertContainer = new AlertContainer();

    public JoinScene() {
        super(new StackPane(alertContainer, container), 1280, 720);
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

    private static class InputField extends TextField {
        private InputField(String promptText) {
            super();
            setPromptText(promptText);
            setFont(Font.loadFont(Inter_Light, 40));
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
                MediaController.playMediaOnce(MediaController.buttonClickSound);
                if (username.getText().isBlank()) {
                    alertContainer.alert("Username can't be blank.");
                    return;
                }
                try {
                    // Create client using host IP address, might throw ConnectionRefusedException
                    Main.createClient(ipAddress.getText());
                    // Send join request packet to server
                    Main.getClient().sendJoinRequestPacket(username.getText());
                    // Go to lobby scene
                    Main.goToLobbyScene();
                } catch (ConnectionRefusedException ex) {
                    alertContainer.alert("Connection refused.");
                }
            });
        }
    }

    private static class CreateServerButton extends Button {
        private CreateServerButton() {
            super("Create Server");
            AtomicBoolean isServerCreated = new AtomicBoolean(false);

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
                MediaController.playMediaOnce(MediaController.buttonClickSound);
                if (isServerCreated.get()) {
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
                isServerCreated.set(!isServerCreated.get());
            });
        }
    }

    private static class AlertContainer extends BorderPane {
        private static final Label alertLabel = new Label();
        static {
            alertLabel.setFont(Font.loadFont(Inter_Regular, 18));
            alertLabel.setTextFill(Color.WHITE);
            alertLabel.setBackground(new Background(new BackgroundFill(Color.valueOf("FF0000"), new CornerRadii(10), null)));
            alertLabel.setPadding(new Insets(10));
            alertLabel.setAlignment(Pos.CENTER);
        }

        private void alert(final String alertMessage) {
            alertLabel.setText(alertMessage);
            new Thread(() -> {
                Platform.runLater(() -> setRight(alertLabel));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
                Platform.runLater(() -> setRight(null));
            }, "alert thread").start();
        }

        private AlertContainer() {
            super();
            setPadding(new Insets(10));
        }
    }
}