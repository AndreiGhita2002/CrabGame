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
    private static int W = 1000, H = 1000;
    private static final int tileWidthPx  = 32; // tile width in px
    private static final int tileHeightPx = 32; // tile height in px
    private static final int zoomFactor = 2;    // kinda weird if 3; fix later
    private static final int tileWidth  = tileWidthPx * zoomFactor;   // tile width
    private static final int tileHeight = tileHeightPx * zoomFactor;  // tile height

    private static Integer offsetX = 0;
    private static Integer offsetY = 0;

    private static Integer topCornerXPx;
    private static Integer topCornerYPx;

    private static Dungeon dungeon;

    private static Entity hero;

    private static SoundPlayer soundPlayer;

    private static boolean running, goNorth, goSouth, goEast, goWest;

    @Override
    public void start(Stage stage) {

        dungeon = new Dungeon();

        hero = new Entity("file:resources/textures/small_hero.png");

        soundPlayer = new SoundPlayer();

        topCornerXPx = W / 2 - (dungeon.getCurrentRoom().sizeX / 2 * tileWidth);
        topCornerYPx = H / 2 - (dungeon.getCurrentRoom().sizeY / 2 * tileHeight);

        hero.X = dungeon.getCurrentRoom().startTileX * tileWidth  + topCornerXPx;
        hero.Y = dungeon.getCurrentRoom().startTileY * tileHeight + topCornerYPx;

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

//        scene.widthProperty().addListener();

//        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
//            W = scene.widthProperty().getValue().intValue();
//        });

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    goNorth = true; soundPlayer.playSoundCycle("abracadabra.wav"); break;
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
                case M: soundPlayer.willPlay = !soundPlayer.willPlay; break; // enables/disables sound
                case T: changeRoom("start", hero); break;   // for debug
                case Y: changeRoom("room2", hero); break;   // changes the room
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

    private static void changeRoom(String newRoomName, Entity entity) {
        dungeon.currentRoomName = newRoomName;

        entity.X = dungeon.getCurrentRoom().startTileX * tileWidth  + topCornerXPx;
        entity.Y = dungeon.getCurrentRoom().startTileX * tileHeight + topCornerYPx;

        entity.refresh();

        moveCamera();
    }

    private static void changeRoom(String newRoomName, Entity entity, Integer newX, Integer newY) {
        dungeon.currentRoomName = newRoomName;

        entity.X = newX * tileWidth;
        entity.Y = newY * tileHeight;

        entity.refresh();

        moveCamera();
    }

    private static void movementInput() {
        int dx = 0, dy = 0;

        if (goNorth) dy += zoomFactor;
        if (goSouth) dy -= zoomFactor;
        if (goEast)  dx -= zoomFactor;
        if (goWest)  dx += zoomFactor;
        if (running) { dx *= 3; dy *= 3; }

        Integer topDestTileX = (hero.X - dx - topCornerXPx) / tileWidth;
        Integer topDestTileY = (hero.Y - dy - topCornerYPx) / tileHeight;

        Integer bottomDestTileX = (hero.X - dx + tileWidth - 1 - topCornerXPx) / tileWidth;
        Integer bottomDestTileY = (hero.Y - dy + tileWidth - 1 - topCornerYPx) / tileHeight;

        if (!dungeon.getCurrentRoom().getTile(topDestTileX, topDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(topDestTileX, bottomDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(bottomDestTileX, topDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(bottomDestTileX, bottomDestTileY).solid) {
            hero.X -= dx;
            hero.Y -= dy;
        }

        hero.refresh();

//        soundPlayer.playSoundCycleStart(1, "abracadabra.wav");
//        soundPlayer.playSoundTest("abracadabra.wav");

//        System.out.print(destTileX);
//        System.out.print(" ");
//        System.out.println(destTileY);

//        moveCamera(); doesn't work at the moment; come back later
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
                changeRoom(effect.effectText, entity);
                break;
            case COORD_CHANGE:
                String[] coords = effect.effectText.split(" ");
                entity.X = Integer.parseInt(coords[0]);
                entity.Y = Integer.parseInt(coords[1]);
                break;
            case ROOM_COORD_CHANGE:
                String[] args = effect.effectText.split(" ");
                changeRoom(args[0], entity, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                break;
            case STATE_CHANGE:
                entity.processEffect(effect);
                break;
        }
    }

    private static void moveCamera() {
        //TODO add camera movement when the room is bigger than the view

        int movementBorder = tileWidth * 3;

        int totalY = H + offsetY;
        int totalX = W + offsetX;

        if (hero.Y > totalY - movementBorder) {  // South Border
            offsetY = H - movementBorder - hero.Y;
        }   // for some reason it follows the hero up

//        if (hero.Y < movementBorder) {  // North Border
//            offsetY = movementBorder + hero.Y;
//        }   // idk atm; doesn't work

        if (hero.X > totalX - movementBorder) {  // East Border
            offsetX = totalX - movementBorder - hero.X;
        }

//        if (hero.X < movementBorder) {  // West Border
//            offsetX = movementBorder + hero.X;
//        }   // idk atm; doesn't work
    }

    private static void drawTile(GraphicsContext g, Tile t, Integer x, Integer y) {
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
                drawTile(g, dungeon.getCurrentRoom().getTile(i, j),
                        i * tileWidth  + offsetX + topCornerXPx,
                        j * tileHeight + offsetY + topCornerYPx);
            }
        }

        //TODO rendering multiple sprites

        g.drawImage(hero.spriteImage, hero.X + offsetX, hero.Y + offsetY, tileWidth, tileHeight);

        drawUI(g);
    }

    private static void drawUI(GraphicsContext g) {
        //TODO drawing the interface
    }

    private static void resetCamera() {
        offsetX = 0;
        offsetY = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
