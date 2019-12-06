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

    private static Dungeon dungeon;

    private static Entity hero;

    private static SoundPlayer soundPlayer;

    private static boolean running, goNorth, goSouth, goEast, goWest;

    @Override
    public void start(Stage stage) {

        dungeon = new Dungeon();

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
                case S: soundPlayer.playSound("abracadabra.wav"); break;
                case F: soundPlayer.playSound("lol_does_not_exist.wav"); break;
                case D: soundPlayer.playSoundTest(); break;
                case M: soundPlayer.willPlay = !soundPlayer.willPlay; break; // enables/disables sound
                case T: changeRoom("start"); break;   // for debug
                case Y: changeRoom("room2"); break;   // changes the room
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movementInput();
                groundEffectPass(hero);
                render(gc);
            }
        };
        timer.start();
    }

    private static void changeRoom(String newRoomName) {
        dungeon.currentRoomName = newRoomName;

        hero.X = dungeon.getCurrentRoom().startTileX * tileWidth;
        hero.Y = dungeon.getCurrentRoom().startTileX * tileHeight;

        hero.refresh();

        moveCamera();
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

        if (!dungeon.getCurrentRoom().getTile(topDestTileX, topDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(topDestTileX, bottomDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(bottomDestTileX, topDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(bottomDestTileX, bottomDestTileY).solid) {
            hero.X -= dx;
            hero.Y -= dy;
        }

        hero.refresh();

        moveCamera();
    }

    private static void groundEffectPass(Entity entity) {
        Integer entityTileX = entity.X / tileWidth;
        Integer entityTileY = entity.Y / tileHeight;

        Effect groundEffect = dungeon.getCurrentRoom().getTileEffect(entityTileX, entityTileY);

        processEffect(entity, groundEffect);
    }

    private static void processEffect(Entity entity, Effect effect) {
        switch (effect.type) {
            case NOTHING:
                break;
            case ROOM_CHANGE:
                changeRoom(effect.effectText);
                break;
            case COORD_CHANGE:
                String[] coords = effect.effectText.split(" ");
                entity.X = Integer.parseInt(coords[0]);
                entity.Y = Integer.parseInt(coords[1]);
                break;
            case ROOM_COORD_CHANGE:
                String[] args = effect.effectText.split(" ");
                changeRoom(args[0]);
                entity.X = Integer.parseInt(args[1]);
                entity.Y = Integer.parseInt(args[2]);
                break;
            case STATE_CHANGE:
                entity.processEffect(effect);
                break;
        }
    }

    private static void moveCamera() {
        //TODO add camera movement when the room is bigger than the view

    }

    private static void drawTile(GraphicsContext g, Tile t, int x, int y) {
        // map Tile from the tile set
        int mx = t.id / 10;
        int my = t.id % 10;

        g.drawImage(dungeon.getCurrentRoom().tileSet, mx * tileWidthPx, my * tileHeightPx, tileWidthPx, tileHeightPx,
                x, y, tileWidth, tileHeight);
    }

    private static void render(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, W, H);

        for (int i = 0; i < dungeon.getCurrentRoom().sizeY; i++) {
            for (int j = 0; j < dungeon.getCurrentRoom().sizeX; j++) {
                drawTile(g, dungeon.getCurrentRoom().getTile(i, j), i * tileWidth + cameraX, j * tileHeight + cameraY);
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
