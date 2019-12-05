package com.company;

enum EffectType {
    ROOM_CHANGE,
    COORD_CHANGE,
    STATE_CHANGE,
    NOTHING,
}

class Effect {

    EffectType type;
    // Types:
    // ROOM_CHANGE  = room teleport into another room
    //              - effectText should be the name of the destination room
    // COORD_CHANGE = teleports you somewhere in the same room
    //              - effectText should be the new coordinates separated by space
    // STATE_CHANGE = state change effect
    //              - nothing to change atm
    //              - this is the most complex effect type
    // NOTHING      = does nothing

    String effectText; //TODO add ground effects (later)

    Effect(EffectType effectType, String effectText) {
        this.effectText = effectText;
        this.type = effectType;
    }

    Effect(EffectType effectType) {
        this(effectType, "");

    }

    Effect() {
        this(EffectType.NOTHING, "");
    }
}
