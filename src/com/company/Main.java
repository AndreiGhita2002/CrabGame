package com.company;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private static PlayerState playerState;

    private static SoundPlayer soundPlayer;

    private static UIData UIData;

    private static boolean running, goNorth, goSouth, goEast, goWest;
    private static boolean canMove = true;

    @Override
    public void start(Stage stage) {

        dungeon = new Dungeon();
        UIData = new UIData();
        hero = new Entity("file:resources/textures/Base_Crab", "hero");
        playerState = new PlayerState();
        soundPlayer = new SoundPlayer();

        topCornerXPx = W / 2 - (dungeon.getCurrentRoom().sizeX / 2 * tileWidth);
        topCornerYPx = H / 2 - (dungeon.getCurrentRoom().sizeY / 2 * tileHeight);

        hero.collisionBoxWidth  = 1.0;
        hero.collisionBoxHeight = 0.3;

        hero.X = dungeon.getCurrentRoom().startTileX * tileWidth  + topCornerXPx;
        hero.Y = dungeon.getCurrentRoom().startTileY * tileHeight + topCornerYPx;
        playerState.setSpawn(hero.X, hero.Y);

        resetCamera();

        Group root = new Group();
        Scene scene = new Scene(root, W, H);
        Canvas canvas = new Canvas(W, H);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);
        stage.setScene(scene);
        stage.show();
        render(gc);

        // for testing: doesn't work atm
        Entity testEntity = new Entity("file:resources/textures/npc_02.png", "npc2", getCoordsFromTileX(3), getCoordsFromTileY(4));
        dungeon.getCurrentRoom().entityList.add(testEntity);

        //Test soundPlayer
        //SoundPlayer.playSoundTest("abracadabra.wav");

//        scene.widthProperty().addListener();

//        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
//            W = scene.widthProperty().getValue().intValue();
//        });

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
                case E: doInteract(); break;
                case F: soundPlayer.playSound("lol_does_not_exist.wav"); break;
                case M: soundPlayer.willPlay = !soundPlayer.willPlay; break; // enables/disables sound
                case T: changeRoom("start", hero); break;   // for debug
                case Y: changeRoom("room2", hero); break;   // changes the room
                case ENTER: if (UIData.dialogueMode) UIData.nextLine(); break; // next line when dialogue is showed
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

    private static void doInteract() {

        Integer targetTileX = getTileFromCoordsX(hero.X);
        Integer targetTileY = getTileFromCoordsY(hero.Y);

        switch (hero.facing) {
            case NORTH:
                targetTileY--;
                break;
            case SOUTH:
                targetTileY++;
                break;
            case WEST:
                targetTileX--;
                break;
            case EAST:
                targetTileX++;
                break;
        }

        System.out.println("hero interacted with: " + targetTileX + ", " + targetTileY);

        // tile interaction
        Tile tile = dungeon.getCurrentRoom().getTile(targetTileX, targetTileY);
        if (!tile.doEffectOnStep) processEffect(hero, tile.effect);


        // entity interaction
//        for (Entity entity : dungeon.getCurrentRoom().entityList) {
//            if (getTileFromCoordsX(entity.X).equals(targetTileX) && getTileFromCoordsY(entity.Y).equals(targetTileY)) {
//
//                System.out.println("hero interacted with entity " + entity.name + " at coords " + targetTileX + ", " + targetTileY);
//
//                processEffect(hero, entity.interact());
//            }
//        }
    }

    private static void changeRoom(String newRoomName, Entity entity) {
        dungeon.currentRoomName = newRoomName;

        entity.X = dungeon.getCurrentRoom().startTileX * tileWidth  + topCornerXPx;
        entity.Y = dungeon.getCurrentRoom().startTileX * tileHeight + topCornerYPx;

        entity.resetSpeed();
        entity.refresh();

        moveCamera();
    }

    private static void changeRoom(String newRoomName, Entity entity, Integer newTileX, Integer newTileY) {
        dungeon.currentRoomName = newRoomName;

        entity.X = getCoordsFromTileX(newTileX);
        entity.Y = getCoordsFromTileY(newTileY);

        entity.resetSpeed();
        entity.refresh();

        moveCamera();
    }

    private static void movementInput() {
        if (!canMove) {
            hero.refresh();
            return;
        }

        int dx = 0, dy = 0;

        // calculating the destination X and Y
        if (goNorth) { dy += zoomFactor; hero.facing = Facing.NORTH; }
        if (goSouth) { dy -= zoomFactor; hero.facing = Facing.SOUTH; }
        if (goEast)  { dx -= zoomFactor; hero.facing = Facing.EAST; }
        if (goWest)  { dx += zoomFactor; hero.facing = Facing.WEST; }
        if (running) { dx *= 3; dy *= 3; }

        //TODO make speed smoother

        // calculating the collision box of the hero after movement:
        Integer topDestTileX = getTileFromCoordsX(hero.X - dx);
        Integer topDestTileY = getTileFromCoordsY(hero.Y - dy);

        Integer bottomDestTileX = getTileFromCoordsX(hero.X - dx + tileWidth  - 1);
        Integer bottomDestTileY = getTileFromCoordsY(hero.Y - dy + tileHeight - 1);

        // checks if any of the corners of the collision box are part of any solid tile
        if (!dungeon.getCurrentRoom().getTile(topDestTileX, topDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(topDestTileX, bottomDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(bottomDestTileX, topDestTileY).solid
                && !dungeon.getCurrentRoom().getTile(bottomDestTileX, bottomDestTileY).solid) {
            hero.X -= dx;  // changes the coords of the hero
            hero.Y -= dy;
        }

        hero.refresh();

//        moveCamera(); doesn't work at the moment; come back later
    }

    private static void groundEffectPass(Entity entity) {
        Integer entityTileX = getTileFromCoordsX(hero.X);
        Integer entityTileY = getTileFromCoordsY(hero.Y);
        Tile tile = dungeon.getCurrentRoom().getTile(entityTileX, entityTileY);

        if (tile.doEffectOnStep) processEffect(entity, tile.effect);
    }

    private static void processEffect(Entity entity, Effect effect) {
        switch (effect.type) {
            case NOTHING:
                break;
            case ROOM_CHANGE:
                changeRoom(effect.effectText, entity);
                entity.resetSpeed();
                break;
            case COORD_CHANGE:
                String[] coords = effect.effectText.split(" ");
                entity.X = Integer.parseInt(coords[0]);
                entity.Y = Integer.parseInt(coords[1]);
                entity.resetSpeed();
                break;
            case ROOM_COORD_CHANGE:
                String[] args = effect.effectText.split(" ");
                changeRoom(args[0], entity, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                entity.resetSpeed();
                break;
            case STATE_CHANGE:
                entity.processEffect(effect);
                break;
            case DIALOGUE:
                UIData.initDialogue(dungeon.getCurrentRoom().getDialogue(effect.effectText));
                canMove = false;
                break;
            case PLAY_ANIMATION:
                entity.doAnimation(effect.effectText);
                break;
            case PRINT_MESSAGE:
                System.out.println(effect.effectText);
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
        // drawing a black background
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, W, H);

        // iterates through the every tile in the current room and draws the tiles at the correct coordinates
        for (int i = 0; i < dungeon.getCurrentRoom().sizeY; i++) {
            for (int j = 0; j < dungeon.getCurrentRoom().sizeX; j++) {
                drawTile(g, dungeon.getCurrentRoom().getTile(i, j),
                        i * tileWidth  + offsetX + topCornerXPx,
                        j * tileHeight + offsetY + topCornerYPx);
            }
        }

        // drawing the hero sprite first
        g.drawImage(hero.getSpriteImage(), hero.X + offsetX, hero.Y + offsetY, tileWidth, tileHeight);

        // drawing all other entities
        for (Entity entity : dungeon.getCurrentRoom().entityList) {
            g.drawImage(entity.getSpriteImage(), entity.X + offsetX, entity.Y + offsetY, tileWidth, tileHeight);
        }

        // drawing the UI
        drawUI(g);
    }

    private static void drawUI(GraphicsContext g) {
        g.setFill(Color.LIGHTGRAY);
        g.setFont(Font.getDefault());

        // Printing a bunch of values (for debug)
        String coordText = "Tile Coords: " + getTileFromCoordsX(hero.X) + ", " + getTileFromCoordsY(hero.Y);
        g.fillText(coordText, 10, 10);
        String facingText = "Facing: " + hero.facing;
        g.fillText(facingText, 10, 23);
        String soundText = "Sound: " + soundPlayer.willPlay;
        g.fillText(soundText, 10, 36);
//        String speedText = "Speed: " + hero.speedX + ", " + hero.speedY;
//        g.fillText(speedText, 10, 49);

        if (UIData.dialogueMode) {
            //drawing the dialogue box
            g.setFill(new Color(0.0, 0.0, 0.0, 0.6));
            g.setStroke(Color.DARKCYAN);
            g.fillRect(150, 700, 700, 200);

            //drawing the text
            g.setFill(Color.LIGHTGRAY);
            g.setFont(new Font("Andale Mono", 30));
            //TODO special behavior for all the line types (atm it just prints them)
            g.fillText(UIData.currentLine().text, 170, 740);
        }
    }

    private static void resetCamera() {
        offsetX = 0;
        offsetY = 0;
    }

    private static Integer getTileFromCoordsX(Integer coordsX) {
        return (coordsX - topCornerXPx) / tileWidth;

    }

    private static Integer getTileFromCoordsY(Integer coordsY) {
        return (coordsY - topCornerYPx) / tileHeight;

    }

    private static Integer getCoordsFromTileX(Integer tileX) {
        return tileX * tileWidth + topCornerXPx;

    }

    private static Integer getCoordsFromTileY(Integer tileY) {
        return tileY * tileHeight + topCornerYPx;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
