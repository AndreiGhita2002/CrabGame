package com.company;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;

enum Facing {
    SOUTH,
    WEST,
    EAST,
    NORTH
}

/* Sprite Entity
 * is used for: Characters and interactive things
 */
class Entity extends ImageView {
    Integer X;
    Integer Y;
    Integer speedX;
    Integer speedY;
    Facing facing;
    String name;

    // by how much to decrease speed each tick
    final static double FRICTION = 0.5;  //TODO implement this

    //this should be used for calculating the collision box
    Double collisionBoxHeight = 1.0;  // in Sizes of tiles
    Double collisionBoxWidth  = 1.0;  // this * AMP

    // south - 0
    // east  - 1
    // north - 2
    // west  - 3
    private List<Image> spriteImages;

    private Effect interactEffect;

    void refresh() {
//        X -= speedX;
//        Y -= speedY;

        relocate(X, Y);

//        if (speedX >  1) speedX -= (int)(FRICTION * speedX);
//        if (speedX < -1) speedX -= (int)(FRICTION * speedX);
//        if (speedY >  1) speedY -= (int)(FRICTION * speedY);
//        if (speedY < -1) speedY -= (int)(FRICTION * speedY);


        // do moving animation
    }

    void resetSpeed() {
        speedX = 0;
        speedY = 0;
    }

    Image getSpriteImage() {
        switch (facing) {
            case SOUTH:
                return spriteImages.get(0);
            case EAST:
                return spriteImages.get(1);
            case NORTH:
                return spriteImages.get(2);
            case WEST:
                return spriteImages.get(3);
        }
        return spriteImages.get(0);
    }

    void processEffect(Effect effect) {
        // T O D O at some point in the far future
        // only god knows what
    }

    Effect interact() {
        return interactEffect;

    }

    void doAnimation(String animationName) {
        //TODO animation

    }

    Entity(String imageUrl, String name) {
        // gets all the sprites
        // TODO at some point it should be changed to a tileset that also supports animations
        spriteImages = new ArrayList<>();
        spriteImages.add(new Image(imageUrl + "_South.png"));
        spriteImages.add(new Image(imageUrl + "_East.png"));
        spriteImages.add(new Image(imageUrl + "_North.png"));
        spriteImages.add(new Image(imageUrl + "_West.png"));

        this.name = name;

        interactEffect = new Effect(EffectType.NOTHING);
        facing = Facing.SOUTH;
        speedX = 0;
        speedY = 0;
        this.X = 0;
        this.Y = 0;
    }

    Entity(String imageURL, String name, Integer X, Integer Y) {
        this(imageURL, name);
        this.X = X;
        this.Y = Y;
    }
}
