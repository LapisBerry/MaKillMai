package app;

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
public class MediaController {
    // Preload all musics
    // Music should play forever

    // Set volume for music
    static {

    }
    // preload all sound fxs
    // sound fx should play one time

    // Set volume for sound fx
    static {

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
