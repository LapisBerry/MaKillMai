package com.lapisberry.gui.scenes;

import com.lapisberry.Main;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static com.lapisberry.gui.FontPreloader.Inter_Black;

public class ServerDisconnectScene extends Scene {
    public ServerDisconnectScene() {
        super(new StackPane(new Title("Server disconnect")), Main.getPrimaryStage().getScene().getWidth(), Main.getPrimaryStage().getScene().getHeight());
    }

    private static class Title extends Text {
        private Title(String text) {
            super(text);
            setFont(Font.loadFont(Inter_Black, 128));
            setTextAlignment(TextAlignment.CENTER);
        }
    }
}
