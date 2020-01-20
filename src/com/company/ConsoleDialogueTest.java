package com.company;

import javafx.application.Application;
import javafx.stage.Stage;

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

    private static void printRoom(Room room) {
        System.out.println(room.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
