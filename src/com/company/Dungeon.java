package com.company;

import java.util.ArrayList;
import java.util.List;

class Dungeon {

    /*
    The purpose of this class is to store all rooms and initialize them based on instructions gave by a file (hopefully json)

    The file format should be:
        <WIP>

     */

    List<Room> roomList = new ArrayList<>();

    String currentRoomName;

    void readFiles() {
        //TODO file reading

    }

    void checkRoomchange() {
        //TODO room change detect thing

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

    void testInit() {
        currentRoomName = "start";

        roomList.add(new Room(10, 10, "start", "resources/map_room1.txt"));
        roomList.add(new Room(10, 10, "room2", "resources/map_room2.txt"));

        linkRooms("start", "room2");
        linkRooms("room2", "start");
    }

    Dungeon() {
        testInit();

//        readFiles();
    }
}
