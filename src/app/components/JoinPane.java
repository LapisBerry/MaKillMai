package app.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class JoinPane extends StackPane {
    public JoinPane() {
        super();
        Title title = new Title("MaKillMai");
        InputField username = new InputField("username");
        InputField ipAddress = new InputField("ip-address");
        JoinButton joinButton = new JoinButton("Join");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(title, username, ipAddress, joinButton);
        vbox.setMaxWidth(400);
        vbox.setMaxHeight(200);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(17);

        getChildren().add(vbox);
    }

    private static class Title extends Text {
        private Title(String text) {
            super(text);
            setFont(Font.loadFont(ClassLoader.getSystemResource("fonts/Inter-Black.ttf").toString(), 128));
            setTextAlignment(TextAlignment.CENTER);
            requestFocus();
        }
    }

    private static class InputField extends TextField {
        private InputField(String promptText) {
            super();
            setPromptText(promptText);
            setFont(Font.loadFont(ClassLoader.getSystemResource("fonts/Inter-ExtraLight.ttf").toString(), 40));
            setBackground(new Background(new BackgroundFill(Color.valueOf("D9D9D9"), new CornerRadii(10), null)));
            setMaxWidth(400);
            setMinHeight(80);
            setAlignment(Pos.CENTER);
        }
    }

    private static class JoinButton extends Button {
        private JoinButton(String text) {
            super(text);
            setFont(Font.loadFont(ClassLoader.getSystemResource("fonts/Inter-SemiBold.ttf").toString(), 40));
            setBackground(new Background(new BackgroundFill(Color.valueOf("00C2FF"), new CornerRadii(40), null)));
            setMaxWidth(274);
            setMinHeight(80);
            setAlignment(Pos.CENTER);
            setOnMouseEntered(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("00A6D1"), new CornerRadii(40), null)));
                setCursor(javafx.scene.Cursor.HAND);
            });
            setOnMouseExited(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("00C2FF"), new CornerRadii(40), null)));
            });
            setOnMousePressed(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("0089A9"), new CornerRadii(40), null)));
            });
            setOnMouseReleased(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("00A6D1"), new CornerRadii(40), null)));
            });
        }
    }
}
