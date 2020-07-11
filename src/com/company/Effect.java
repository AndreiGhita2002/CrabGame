package com.company;

enum EffectType {
    ROOM_CHANGE,
    COORD_CHANGE,
    ROOM_COORD_CHANGE,
    STATE_CHANGE,
    DIALOGUE,
    PLAY_ANIMATION,
    PLAY_SOUND,
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
    //                   - the effect text is the tag then the value separated by space
    // DIALOGUE          = opens a dialog window and starts the dialog process
    //                   - effectText is the dialog name
    // PLAY_ANIMATION    = plays an animation
    //                   - the effectText is the animation name
    // PLAY_SOUND        = plays a sound
    //                   - the effectText is the sound name
    // PRINT_MESSAGE     = prints a message to the console (only for debug)
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
