package com.company;

import javafx.scene.image.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


class Room {

    List<String> adjacentRooms = new ArrayList<>();
    String name; // no spaces

    Image tileSet;

    Integer startTileX;
    Integer startTileY;
    Integer sizeX;
    Integer sizeY;

    private List<List<Tile>> tileMap;

    List<Entity> entityList = new ArrayList<>();
    HashMap<String, Dialogue> dialogueList = new HashMap<>();

    void addAdjacentRoom(Room room) {
        adjacentRooms.add(room.name);

    }

    private void getTileSetImage() {
        tileSet = new Image("file:resources/textures/tileset.png");

    }

    private void readFile(String path) {
        // reads the tiles and their solidity from a plain text file
        // the format for the file is as follows:
        // 1. a matrix of numbers that represent the tile IDs
        // 2. the character 'e' to denote the end of the first matrix
        // 3. a matrix of 1s and 0s that denote the solidity of the tiles
        // 4. the character 'e' to denote the end of the second matrix
        // 5. two numbers that represent the default entering coordinates

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

            String str;

            // for tiles
            for (List<Tile> tiles : tileMap) {
                if (!(tileMap.indexOf(tiles) == 0 || tileMap.indexOf(tiles) == sizeY - 1)) { // does not read for the boundaries

                    str = reader.readLine();
                    String[] input = str.split(" ");

                    for (int i = 1; i < input.length; i++) {
                        tiles.get(i).id = Integer.parseInt(input[i]);
                    }
                }
            }

            // reads until the separator
            do {
                str = reader.readLine();
            } while (!str.equals("e"));

            // for solidity
            for (List<Tile> tiles : tileMap) {
                if (!(tileMap.indexOf(tiles) == 0 || tileMap.indexOf(tiles) == sizeY - 1)) { // does not read for the boundaries

                    str = reader.readLine();
                    String[] input = str.split(" ");

                    for (int i = 1; i < input.length; i++) {
                        if (input[i].equals("1")) {
                            tiles.get(i).solid = true;
                        }
                    }
                }
            }

            // reads until the separator
            do {
                str = reader.readLine();
            } while (!str.equals("e"));

            // reading the start tiles
            str = reader.readLine();
            String[] startCoords = str.split(" ");
            startTileX = Integer.parseInt(startCoords[0]);
            startTileY = Integer.parseInt(startCoords[1]);

            // reading the dialogue
            String line = reader.readLine();
            String[] words = line.split(" ");
            String currentDialogueName = "";
            StringBuilder body = new StringBuilder();
            boolean isFirst = true;

            while (!words[0].equals("end")) {

                switch (words[0]) {
                    case "!":   // new dialogue start
                        if (!isFirst) {
                            dialogueList.get(currentDialogueName).setBody(body.toString());
                            body = new StringBuilder();
                        }

                        dialogueList.put(words[1], new Dialogue(words[1]));
                        currentDialogueName = words[1];
                        isFirst = false;
                        break;

                    case ".":   // new dialogue line
                    case "-":   // effect
                    case "?":   // new dialogue question
                    case ">":   // dialogue option
                        body.append(line).append("\n");
                        break;
                }
                line = reader.readLine();
                words = line.split(" ");
            }

            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String toString() {
        StringBuilder out = new StringBuilder();

        for (List<Tile> tiles : tileMap) {
            for (Tile tile : tiles) {

                out.append(tile.id);
                out.append("|");
                out.append(tile.solid);
                out.append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }

    void setTileEffect(Integer X, Integer Y, Effect effect) {
        getTile(X, Y).stepOnEffect = effect;

        //for debug:
//        getTile(X, Y).id = 5;
    }

    void setTileEffect(String string) {
//        System.out.println("room " + name + " has called setTileEffect() with the arg: " + string);
        // string example: roomChange-1-1-room2-6-3

        String[] words = string.split("-");

        switch (words[0]) {
            case "ROOM_COORD_CHANGE":
                Integer coordX = Integer.parseInt(words[1]);
                Integer coordY = Integer.parseInt(words[2]);
                Effect effect  = new Effect(EffectType.ROOM_COORD_CHANGE, words[3] + " " + words[4] + " " + words[5]);
                setTileEffect(coordX, coordY, effect);
                break;
            default:
                System.out.println("something wrong in Room.setTileEffect()");
                break;
        }

        //TODO finish setTileEffect()
    }

    Tile getTile(Integer X, Integer Y) {

        // if the tile requested then it returns a solid tile of ID 14
        if (X < 0 || Y < 0 || X >= sizeX || Y >= sizeY) {
            return new Tile(14, true);
        }

        return tileMap.get(Y).get(X);

    }

    Effect getTileEffect(Integer x, Integer y) {
        return getTile(x, y).stepOnEffect;

    }

    Boolean hasDialogue(String dialogueName) {
        AtomicReference<Boolean> out = new AtomicReference<>(false);

        dialogueList.forEach((k, v)-> {
            if (k.equals(dialogueName)) {
                out.set(true);
            }
        });
        return out.get();
    }

    Dialogue getDialogue(String dialogueName) {
        AtomicReference<Dialogue> out = new AtomicReference<>();

        dialogueList.forEach((k, v) -> {
            if (k.equals(dialogueName)) {
                out.set(v);
            }
        });
        return out.get();
    }

    void printDialogText() {

        dialogueList.forEach((k, v)-> {
            System.out.println("Dialogue " + k + " has body: ");
            System.out.println(v.toString());
            System.out.println();
        });
    }

    Room(Integer setSizeX, Integer setSizeY, String name, String path) {

        this.name = name;

        sizeX = setSizeX + 2;
        sizeY = setSizeY + 2;

        // init list list
        tileMap = new ArrayList<>();

        // adding boundaries
        for (int i = 0; i < sizeY; i++) {
            tileMap.add(new ArrayList<>());

            for (int j = 0; j < sizeX; j++) {
                if (i == 0 || j == 0 || i == sizeY - 1 || j == sizeX - 1) {
                    tileMap.get(i).add(new Tile(3, true));
                } else {
                    tileMap.get(i).add(new Tile());
                }
            }
        }

        readFile(path);

        // get tileSet image
        getTileSetImage();

        // for testing
//        printDialogText();
    }
}
