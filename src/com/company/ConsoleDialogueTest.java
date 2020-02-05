package com.company;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  This is used for testing the dialogue system only from a text interface.
 */
public class ConsoleDialogueTest extends Application {

    static Dungeon dungeon;
    static Scanner scanner;

    @Override
    public void start(Stage stage) {
        dungeon = new Dungeon();

        String line;
        String[] words;
        scanner = new Scanner(System.in);
        boolean willRun = true;

        while (willRun) {
            line  = scanner.nextLine();
            words = line.split(" ");

            switch (words[0]) {
                case "dia":
                    if (dungeon.getCurrentRoom().hasDialogue(words[1])) {
                        dia(words[1]);
                    } else {
                        System.out.println("no dialogue found that is named like that");
                    }
                    break;
                case "map":
                    printRoom(dungeon.getCurrentRoom());
                    break;
                case "dialist":
                    dungeon.getCurrentRoom().printDialogText();
                    break;
                case "room":
                    System.out.println(dungeon.getCurrentRoom().name);
                    break;
                case "goto":
                    if (dungeon.hasRoom(words[1])) {
                        dungeon.currentRoomName = words[1];
                    } else System.out.println("wrong room name ");
                    break;
                case "exit":
                    willRun = false;
                    break;
            }
        }
    }

    private static void dia(String dialogueName) {

        if (!dungeon.getCurrentRoom().hasDialogue(dialogueName)) {
            System.out.println("wrong dialogue name: " + dialogueName);
            return;
        }

        Dialogue dialogue = dungeon.getCurrentRoom().getDialogue(dialogueName);
        int currentLine = 0;

        while (currentLine < dialogue.body.size()) {
            System.out.print(dialogue.getLine(currentLine));

            if (dialogue.isType(currentLine, '?')) {
                System.out.println();

                ArrayList<String> targetedDialogues = new ArrayList<>();

                currentLine++;

                while (currentLine < dialogue.body.size()) {
                    System.out.print(dialogue.getLine(currentLine));

                    targetedDialogues.add(dialogue.getLine(currentLine).split(" ")[1]);

                    currentLine++;
                    scanner.nextLine();
                }

                while (!targetedDialogues.isEmpty()) {

                    String input = scanner.nextLine();

                    for (String string : targetedDialogues) {
                        if (string.equals(input)) {
                            dia(input);
                            return;
                        }
                    }

                    System.out.println("wrong answer");
                }
            }
            currentLine++;
            scanner.nextLine();
        }
    }

    private static void printRoom(Room room) {
        System.out.println(room.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
