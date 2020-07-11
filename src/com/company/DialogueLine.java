package com.company;

enum DialogueLineType {
    STATEMENT,
    QUESTION,
    CHOICE,
    EFFECT
}

public class DialogueLine {

    DialogueLineType type;
    String text;

    public String toString() {
        return text;
    }

    DialogueLine(DialogueLineType type, String text) {
        this.type = type;
        this.text = text;
    }
}
