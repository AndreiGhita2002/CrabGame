package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Dungeon {

    /*
    The purpose of this class is to store all rooms and initialize them based on instructions gave by a file (hopefully json)

    The file format should be:
        <WIP>

     */

    List<Room> roomList = new ArrayList<>();

    static final String dungeonDirectory = "resources/dungeon";
    static final String dungeonPlanFile  = "dungeon_plan.txt";

    String currentRoomName;

    void readFiles() {

        String dungeonPlanPath = "" + dungeonDirectory + "/" + dungeonPlanFile;

        try {
            BufferedReader planReader = new BufferedReader(new FileReader(new File(dungeonPlanPath)));

            String[] words = planReader.readLine().split(" ");

            // reads the next line and if the line is equal to "end" then it ends the loop
            while(!words[0].equals("end")) {
                if (!words[0].isBlank()) {
                    switch (words[0].charAt(0)) {
                        case '§':   //room declaration
                            String roomName = words[1];
                            String fileName = "resources/dungeon/" + words[2];
                            int sizeX = Integer.parseInt(words[3]);
                            int sizeY = Integer.parseInt(words[4]);

                            roomList.add(new Room(sizeX, sizeY, roomName, fileName));
                            break;
                        case '&':   //room linking
                            linkRooms(words[1], words[2]);
                            break;
                        case '*':   //adding special tiles
                            getRoom(words[1]).setTileEffect(words[2]);
                            break;
                        case '#':
                            break; //comments
                    }
                }
                words = planReader.readLine().split(" ");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    Room getCurrentRoom() {
        return getRoom(currentRoomName);

    }

    Room getRoom(String roomName) {
        for (Room room : roomList) {
            if (room.name.equals(roomName)) {
                return room;
            }
        }
        System.out.println("bruh not good in Dungeon.getRoom()");

        return null;
    }

    void linkRooms(String roomName1, String roomName2) {
        // one directional

        getRoom(roomName1).addAdjacentRoom(getRoom(roomName2));
    }

    Dungeon() {
        currentRoomName = "start";
        readFiles();
    }
}
