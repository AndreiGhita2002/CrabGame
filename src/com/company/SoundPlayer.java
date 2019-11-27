package com.company;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {

    //Play abracadabra.wav test sound
    void playSoundTest (String fileName) {
        Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/resources/sfx/" + fileName);

        MediaPlayer player = new MediaPlayer(m);

//        System.out.println("played " + fileName);

        player.play();



        //TODO this method shouldn't be different from playSound
        // also no statics are needed here (in most cases you don't need them)
    }

    void playSound(String soundName) {
        //TODO plays a sound
        // ^^ the soundName should be the same as the file
        // example: soundName = "move_grass" should play the file move_grass.mov
        // if it doesn't find the file it should throw an error
    }

    void triggerChangeMusic(String musicName) {
        //TODO changes the background music track
    }

    //TODO play sound when player moves
    // ^^ should be called from the Main class
    // this class should not be aware at all of what is happening in Main Class
    // instead Main.movementInput() should call the playSound() method

}
