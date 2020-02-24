package com.company;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

enum Facing {
    DOWN,
    LEFT,
    RIGHT,
    UP
}

/* Sprite Entity
 * is used for: Characters and interactive things
 */
class Entity extends ImageView {
    Integer X;
    Integer Y;
    Facing facing;
    String name;

    //this should be used
    Double collisionBoxHeight = 1.0;  // in Sizes of tiles
    Double collisionBoxWidth  = 1.0;  // this * AMP

    Image spriteImage;

    private Effect interactEffect;

    void refresh() {
        relocate(X, Y);

        // do moving animation
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
        spriteImage = new Image(imageUrl);
        interactEffect = new Effect(EffectType.NOTHING);
        facing = Facing.DOWN;
        this.name = name;
    }

    Entity(String imageURL, String name, Integer X, Integer Y) {
        this(imageURL, name);
        this.X = X;
        this.Y = Y;
    }
}
