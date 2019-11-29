package com.company;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class SoundPlayer {

    Boolean willPlay = true; // enable/disable sound

    //Play abracadabra.wav test sound
    void playSoundTest () {
        if (willPlay) {
            try {
                Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/resources/sfx/" + "abracadabra.wav");
                MediaPlayer player = new MediaPlayer(m);
                player.play();
                System.out.println("Played test sound abracadabra.wav");
            } catch (Exception IOException) {
                System.out.println("Error: abracadabra.wav failed to play");
            }
            //TODO this method shouldn't be different from playSound
        }
    }

    //Play a sound
    void playSound(String soundName) {

        //plays a specific sound
        // ^^ the soundName should be the same as the file
        // example: soundName = "move_grass" should play the file move_grass.mov
        // if it doesn't find the file it should throw an error

        if (willPlay) {
            try {
                Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/resources/sfx/" + soundName);
                MediaPlayer player = new MediaPlayer(m);

                player.play();

                System.out.println("Played file " + soundName);

            } catch (Exception IOException) {
                System.out.println("Error: sound file " + soundName + " not found");
            }
        }
    }

    void walkOnSound(Integer tileID) {
        //TODO plays a sound that corresponds to that tile
        // ^ should import a from a text file what sound file to use for every tileID (or just the ones that make sound)

        if (willPlay) {


            // should get the file name from the text file and then call platSound()
        }

    }

    void triggerChangeMusic(String musicName) {

        //TODO changes the background music track

        if (willPlay) {

        }
    }

    //TODO play sound when player moves
    // ^^ should be called from the Main class
    // this class should not be aware at all of what is happening in Main Class
    // instead Main.movementInput() should call the playSound() method

}
