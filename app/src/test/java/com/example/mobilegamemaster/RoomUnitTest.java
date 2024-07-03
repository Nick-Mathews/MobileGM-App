package com.example.mobilegamemaster;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.mobilegamemaster.Entities.Room;

public class RoomUnitTest {
    Room room = new Room(1, "Prison Break");
    int correctNum = 1;
    int incorrectNum = -1;
    String correctName = "Prison Break";
    String incorrectName = "";
    int getTestRoomID = room.getRoomID();
    String getTestRoomName = room.getRoomName();
    //GET TESTS FOR ROOM ENTITY
    @Test
    public void roomIDisCorrect() {assertEquals(correctNum, getTestRoomID);}
    @Test
    public void roomIDisIncorrect() {assertNotEquals(incorrectNum, getTestRoomID);}
    @Test
    public void roomNameIsCorrect() {assertEquals(correctName, getTestRoomName);}
    @Test
    public void roomNameIsIncorrect() {assertNotEquals(incorrectName, getTestRoomName);}

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
}
