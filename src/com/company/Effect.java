package com.company;

enum EffectType {
    ROOM_CHANGE,
    COORD_CHANGE,
    ROOM_COORD_CHANGE,
    STATE_CHANGE,
    DIALOGUE,
    PLAY_ANIMATION,
    PRINT_MESSAGE,
    NOTHING
}

class Effect {

    EffectType type;
    // Types:
    // ROOM_CHANGE       = room teleport into another room
    //                   - effectText should be the name of the destination room
    // COORD_CHANGE      = teleports you somewhere in the same room
    //                   - effectText should be the new coordinates separated by space
    // ROOM_COORD_CHANGE = does the above two at the same time
    //                   - effectText should be the name of the destination room and then the coordinates separated by space
    // STATE_CHANGE      = state change effect
    //                   - nothing to change atm
    //                   - this is the most complex effect type
    // DIALOGUE          = opens a dialog window and starts the dialog process
    //                   - effectText is the dialog name
    // PLAY_ANIMATION    = plays an animation
    //                   - the effectText is the animation name
    // NOTHING           = does nothing

    String effectText;

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
