package com.packages.mobilegamemaster;

import org.junit.Test;

import static org.junit.Assert.*;

import com.packages.mobilegamemaster.Entities.Room;

public class RoomUnitTest {
    Room room = new Room(1, "Prison Break", 3600000);
    int correctNum = 1;
    int incorrectNum = -1;
    String correctName = "Prison Break";
    String incorrectName = "";
    int correctTime = 3600000;
    int incorrectTime = 3600;
    int getTestRoomID = room.getRoomID();
    String getTestRoomName = room.getRoomName();
    int getTestRoomTime = room.getRoomTime();

    //GET TESTS FOR ROOM ENTITY
    @Test
    public void roomIDisCorrect() {assertEquals(correctNum, getTestRoomID);}
    @Test
    public void roomIDisIncorrect() {assertNotEquals(incorrectNum, getTestRoomID);}
    @Test
    public void roomNameIsCorrect() {assertEquals(correctName, getTestRoomName);}
    @Test
    public void roomNameIsIncorrect() {assertNotEquals(incorrectName, getTestRoomName);}
    @Test
    public void roomTimeIsCorrect() {assertEquals(correctTime, getTestRoomTime);}
    @Test
    public void roomTimeIsIncorrect() {assertNotEquals(incorrectTime, getTestRoomTime);}

    //SET TESTS FOR ROOM ENTITY
    @Test
    public void setRoomIDisCorrect() {
        room.setRoomID(2);
        assertEquals(2, room.getRoomID());
    }
    @Test
    public void roomIDIncorrect() {assertNotEquals(incorrectNum, room.getRoomID());}
    @Test
    public void setRoomNameIsCorrect() {
        room.setRoomName("Ivans Lair");
        assertEquals("Ivans Lair", room.getRoomName());
    }
    @Test
    public void roomNameIncorrect() {assertNotEquals(incorrectName, room.getRoomName());}
    @Test
    public void setRoomTimeIsCorrect() {
        room.setRoomTime(5400000);
        assertEquals(5400000, room.getRoomTime());
    }
    @Test
    public void roomTimeIncorrect() {assertNotEquals(incorrectTime, room.getRoomTime());}
}
