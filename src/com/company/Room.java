package com.company;

import javafx.scene.image.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class Room {

    List<String> adjacentRooms = new ArrayList<>();
    String name; // no spaces

    Image tileSet;

    Integer startTileX;
    Integer startTileY;

    private List<List<Tile>> tileMap;

    Integer sizeX;
    Integer sizeY;

    void addAdjacentRoom(Room room) {
        adjacentRooms.add(room.name);

    }

    private void getTileSetImage() {
        tileSet = new Image("file:resources/textures/tileset.png");

    }

    private void getMapFromText(String path) {
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

            str = reader.readLine();
            String[] input = str.split(" ");

            startTileX = Integer.parseInt(input[0]);
            startTileY = Integer.parseInt(input[1]);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (startTileX == null) {
            startTileX = 5;
            System.out.println("something strangeX in " + name + ".getMapFromText()");
        }
        if (startTileY == null) {
            startTileY = 5;
            System.out.println("something strangeY in " + name + ".getMapFromText()");
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

        getMapFromText(path);

        // get tileSet image
        getTileSetImage();
    }
}
