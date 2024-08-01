package com.packages.mobilegamemaster.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rooms")
public class Room {
    @PrimaryKey(autoGenerate = true)
    private int roomID;
    private String roomName;
    private int roomTime;

    public Room (int roomID, String roomName, int roomTime) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomTime = roomTime;
    }

    public int getRoomID() {
        return roomID;
    }
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomTime() {
        return roomTime;
    }
    public void setRoomTime(int roomTime) {
        this.roomTime = roomTime;
    }

}
