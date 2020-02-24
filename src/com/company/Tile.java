package com.company;

class Tile {
    Integer id;

    Boolean solid;

    Effect effect;
    boolean doEffectOnStep = false;

    Tile(Integer id, Boolean solid, Effect effect) {
        this.id = id;
        this.solid = solid;
        this.effect = effect;
    }

    Tile(Integer id, Boolean solid) {
        this(id, solid, new Effect());
    }

    Tile() {
        this(0, false, new Effect());
    }
}
