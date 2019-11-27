package com.company;

import javafx.scene.image.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class Room {

    Image tileSet;

    private List<List<Tile>> tileMap;

    Integer sizeX;
    Integer sizeY;

    private void getTileSetImage() {
        tileSet = new Image("file:resources/tileset.png");

    }

    private void getMapFromText(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

            String str;

            for (List<Tile> tiles : tileMap) {
                if (!(tileMap.indexOf(tiles) == 0 || tileMap.indexOf(tiles) == sizeY - 1)) { // does not read for the boundaries

                    str = reader.readLine();
                    String[] input = str.split(" ");

                    for (int i = 1; i < input.length; i++) {
                        tiles.get(i).id = Integer.parseInt(input[i]);
                    }
                }
            }

            do {
                str = reader.readLine();
            } while (!str.equals("e"));

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

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void outputTileMapText() {
        for (List<Tile> tiles : tileMap) {
            for (Tile tile : tiles) {
                System.out.print(tile.id);
                System.out.print("|");
                System.out.print(tile.solid);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    Tile getTile(Integer X, Integer Y) {
        return tileMap.get(Y).get(X);

    }

    Effect getTileEffect(Integer x, Integer y) {
        return getTile(x, y).stepOnEffect;
    }

    Room(Integer setSizeX, Integer setSizeY, String path) {

        sizeX = setSizeX + 2;
        sizeY = setSizeY + 2;

        // init list list
        tileMap = new ArrayList<>();

        //TODO add a boundary

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

//        outputTileMapText();
    }
}
