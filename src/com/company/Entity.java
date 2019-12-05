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

    void refresh() {
        relocate(X, Y);

        //TODO animation
    }

    void processEffect(Effect effect) {
        // T O D O at some point in the far future
        // only god knows what
    }

    Entity(Image sprite) {
        spriteImage = sprite;
    }

    Entity(String imageUrl) {
        spriteImage = new Image(imageUrl);
    }
}
