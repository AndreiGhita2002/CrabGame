package com.company;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/* Sprite Entity
 * is used for: Characters and interactive things
 */
class Entity extends ImageView {
    Integer X;
    Integer Y;

    //this should be used
    Double collisionBoxHeight = 1.0;  // in Sizes of tiles
    Double collisionBoxWidth  = 1.0;  // this * AMP

    Image spriteImage;

    Effect interactEffect;

    void refresh() {
        relocate(X, Y);

        //TODO animation
    }

    void processEffect(Effect effect) {
        // T O D O at some point in the far future
        // only god knows what
    }

    Effect interact() {
        return interactEffect;
    }

    Entity(Image sprite) {
        spriteImage = sprite;
        interactEffect = new Effect(EffectType.NOTHING);
    }

    Entity(String imageUrl) {
        spriteImage = new Image(imageUrl);
        interactEffect = new Effect(EffectType.NOTHING);
    }

    Entity(String imageURL, Integer X, Integer Y) {
        this(imageURL);
        this.X = X;
        this.Y = Y;
    }
}
