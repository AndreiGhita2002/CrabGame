package com.company;

class Effect {

    Character type;
    // Types:
    // 'I' = impassable (can't move)
    // 'N' = normal     (can move; nothing happens)

    String effectText; //TODO add ground effects (later)

    Boolean allowsMovement() {
        return !type.equals('I');
    }

    Effect(Character effectType, String effectText) {
        this.effectText = effectText;
        this.type = effectType;
    }

    Effect(Character effectType) {
        this(effectType, "");
    }

    Effect() {
        this('N', "");
    }
}
