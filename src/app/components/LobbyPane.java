package app.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LobbyPane extends StackPane {
    public LobbyPane() {
        super();
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
            setOnMouseExited(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("44FF02"), new CornerRadii(40), null)));
            });
            setOnMousePressed(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("2b850c"), new CornerRadii(40), null)));
            });
            setOnMouseReleased(e -> {
                setBackground(new Background(new BackgroundFill(Color.valueOf("44FF02"), new CornerRadii(40), null)));
            });
        }
    }
}
