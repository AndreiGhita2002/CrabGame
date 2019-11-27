package com.company;

class Tile {
    Integer id;

    Boolean solid;

    Effect stepOnEffect;

    Tile(Integer id, Boolean solid) {
        this.id = id;
        this.solid = solid;

        if (solid) {
            stepOnEffect = new Effect('I');
        } else {
            stepOnEffect = new Effect();
        }
    }

    Tile(Integer id) {
        this(id, false);
    }

    Tile() {
        this(0, false);
    }
}
