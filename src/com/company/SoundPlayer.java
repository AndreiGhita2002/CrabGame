package com.company;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class SoundPlayer {

    static void playSoundTest (String fileName) {
        Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/" + "resources" + "/" + "sfx" + "/" + fileName);
        //Media m = new Media("resources\\sfx\\" + fileName);
        MediaPlayer player = new MediaPlayer(m);
        player.play();
    }

    void playSound(String soundName) {
        //TODO plays a sound
    }

    void triggerChangeMusic(String musicName) {
        //TODO changes the background music track
    }

    /*
    scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:     break;
                case DOWN:   break;
                case LEFT:   break;
                case RIGHT:  break;
                case SHIFT:  break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:     break;
                case DOWN:   break;
                case LEFT:   break;
                case RIGHT:  break;
                case SHIFT:  break;
            }
        });
    */
}
