package com.company;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;

class SoundPlayer {

    Boolean willPlay = false; // enable/disable sound

    //Doesn't work yet

    public static void getTileIDSounds() {
        try {
            File tileSoundFile = new File("tilesound.txt");
            Scanner sc = new Scanner(tileSoundFile);

            System.out.println(sc.nextLine());

            //HashMap<String, Integer> TileSoundIDs = new HashMap<String, Integer> ();

            //while (sc.hasNextLine()) {
            //    System.out.println(sc.nextLine());
            //}

            /*while (sc.hasNextLine()) {
                TileSoundIDs.put(sc.nextLine(), sc.nextInt());
                System.out.println("Added to TileSoundIDs HashMap: " + sc.nextLine());
            }*/

            //System.out.println(TileSoundIDs);

        } catch(Exception IOException) {
            System.out.println("Error: Failed to import tile sound IDs");
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
        String soundNameTerrain = "default";

        if (willPlay) {
            try {

                playSound(soundNameTerrain);

            } catch (Exception IOException) {
                System.out.println("Error: Tile ID \"" + tileID + "\" not found");
            }

            // should get the file name from the text file and then call playSound()
        }

    }

    void triggerChangeMusic(String musicName) {

        //TODO changes the background music track

        if (willPlay) {
            try {

            } catch (Exception IOException) {

            }

        }
    }
            //TODO play sound when player moves
            // ^^ should be called from the Main class
            // this class should not be aware at all of what is happening in Main Class
            // instead Main.movementInput() should call the playSound() method
        }
