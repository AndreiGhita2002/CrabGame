package com.company;

public class UIData {

    Dialogue currentDialogue;
    boolean dialogueMode;
    private int nextLine;

    void initDialogue(Dialogue dialogue) {
        currentDialogue = dialogue;
        nextLine = 0;
        dialogueMode = true;

        System.out.println("dialogue initialized with dialogue " + dialogue.name);
    }

    DialogueLine nextLine() {
        if (!dialogueMode) {
            System.out.println("something called UI.nextLine() without doing initDialogue() first ");
            return null;
        }
        nextLine++;
        return currentDialogue.getLine(nextLine);
    }

    DialogueLine currentLine() {
        return currentDialogue.getLine(nextLine);
    }

    void exitDialogue() {
        currentDialogue = null;
        dialogueMode = false;
    }

    UIData() {

    }
}
