package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dialogue {

    String name;
    List<DialogueLine> lines;

    void setBody(String bodyString) {
        String[] fullText = bodyString.split("\n");

        for (String line : fullText) {
            String text = line.split(" ", 2)[1];

            switch (line.charAt(0)) {
                case '.':
                    lines.add(new DialogueLine(DialogueLineType.STATEMENT, text));
                    break;
                case '-':
                    lines.add(new DialogueLine(DialogueLineType.EFFECT, text));
                    break;
                case '?':
                    lines.add(new DialogueLine(DialogueLineType.QUESTION, text));
                    break;
                case '>':
                    lines.add(new DialogueLine(DialogueLineType.CHOICE, text));
                    break;
            }
        }
    }

    boolean isType(int lineNumber, DialogueLineType type) {
        return getLine(lineNumber).type.equals(type);
    }

    DialogueLine getLine(int lineNumber) {
        return lines.get(lineNumber);
    }

    Dialogue(String name) {
        lines = new ArrayList<>();
        this.name = name;
    }
}
