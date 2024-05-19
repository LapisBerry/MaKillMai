package com.lapisberry.gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * The {@code MediaController} class is used to trigger the media player
 * <p>
 * Example of usage:
 * <pre>
 *     import application.MediaController;
 *     MediaController.play(MediaController.monkeysSpinningMonkeysMusic);
 */
public final class MediaController {
    // Preload all musics
    // Music should play forever

    // Set volume for music
    static {

    }
    // preload all sound fxs
    // sound fx should play one time
    public static final MediaPlayer buttonClickSound = loadRes("audios/button-click.mp3");
    // Set volume for sound fx
    static {
        buttonClickSound.setVolume(0.8);
    }


    // Methods
    public static void playMediaOnce(MediaPlayer mediaPlayer) {
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    }

    public static void playMediaForever(MediaPlayer mediaPlayer) {
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public static void stop(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
    }

    private static MediaPlayer loadRes(String path) {
        return new MediaPlayer(new Media(ClassLoader.getSystemResource(path).toString()));
    }
}