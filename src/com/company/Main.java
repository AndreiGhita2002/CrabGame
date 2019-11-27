package com.company;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.*;

public class Main extends Application {

    // TODO add window resizing
    private static final int W = 600, H = 600;
    private static final int tileWidthPx  = 32; // tile width in px
    private static final int tileHeightPx = 32; // tile height in px
    private static final int zoomFactor = 2;
    private static final int tileWidth  = tileWidthPx * zoomFactor;   // tile width
    private static final int tileHeight = tileHeightPx * zoomFactor;  // tile height

    private static Integer cameraX;
    private static Integer cameraY;

    private static Room currentRoom;

    private static Entity hero;

    private static SoundPlayer soundPlayer;

    private static boolean running, goNorth, goSouth, goEast, goWest;

    @Override
    public void start(Stage stage) {
        currentRoom = new Room(10, 10, "resources/tilemap.txt");
        hero = new Entity("file:resources/small_hero.png");

        soundPlayer = new SoundPlayer();

        hero.X = W / 2;
        hero.Y = H / 2;

        resetCamera();

        Group root = new Group();
        Scene scene = new Scene(root, W, H);
        Canvas canvas = new Canvas(W, H);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);
        stage.setScene(scene);
        stage.show();
        render(gc);


        //Test soundPlayer
        //SoundPlayer.playSoundTest("abracadabra.wav");

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    goNorth = true; break;
                case DOWN:  goSouth = true; break;
                case LEFT:  goWest  = true; break;
                case RIGHT: goEast  = true; break;
                case SHIFT: running = true; break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:    goNorth = false; break;
                case DOWN:  goSouth = false; break;
                case LEFT:  goWest  = false; break;
                case RIGHT: goEast  = false; break;
                case SHIFT: running = false; break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movementInput();

                render(gc);
            }
        };
        timer.start();
    }

    private static void movementInput() {
        int dx = 0, dy = 0;

        if (goNorth) dy += zoomFactor;
        if (goSouth) dy -= zoomFactor;
        if (goEast)  dx -= zoomFactor;
        if (goWest)  dx += zoomFactor;
        if (running) { dx *= 3; dy *= 3; }

        Integer topDestTileX = (hero.X - dx) / tileWidth;
        Integer topDestTileY = (hero.Y - dy) / tileHeight;

        Integer bottomDestTileX = (hero.X - dx + tileWidth - 1) / tileWidth;
        Integer bottomDestTileY = (hero.Y - dy + tileWidth - 1) / tileHeight;

        if (!currentRoom.getTile(topDestTileX, topDestTileY).solid
                && !currentRoom.getTile(topDestTileX, bottomDestTileY).solid
                && !currentRoom.getTile(bottomDestTileX, topDestTileY).solid
                && !currentRoom.getTile(bottomDestTileX, bottomDestTileY).solid) {
            hero.X -= dx;
            hero.Y -= dy;
        }

        hero.refresh();

//        soundPlayer.playSoundTest("abracadabra.wav");

//        System.out.print(destTileX);
//        System.out.print(" ");
//        System.out.println(destTileY);

        moveCamera();
    }

    private static void moveCamera() {
        //TODO add camera movement when the room is bigger than the view

    }

    private static void drawTile(GraphicsContext g, Tile t, int x, int y) {
        // map Tile from the tile set
        int mx = t.id / 10;
        int my = t.id % 10;

        g.drawImage(currentRoom.tileSet, mx * tileWidthPx, my * tileHeightPx, tileWidthPx, tileHeightPx,
                x, y, tileWidth, tileHeight);
    }

    private static void render(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, W, H);

        for (int i = 0; i < currentRoom.sizeY; i++) {
            for (int j = 0; j < currentRoom.sizeX; j++) {
                drawTile(g, currentRoom.getTile(i, j), i * tileWidth + cameraX, j * tileHeight + cameraY);
            }
        }

        //TODO rendering multiple sprites

        g.drawImage(hero.spriteImage, hero.X, hero.Y, tileWidth, tileHeight);
    }

    private static void resetCamera() {
//        hero.X = W / 2;
//        hero.Y = W / 2;
        cameraX = 0;
        cameraY = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
