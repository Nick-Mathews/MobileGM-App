package com.example.mobilegamemaster.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName="timers")
public class Timer {
    @PrimaryKey(autoGenerate = true)
    int timerID;
    int roomID;
    String roomName;
    String startTime;
    String endTime;
    String gameDate;
    String timeLeft;

    public Timer(int timerID, int roomID, String roomName, String startTime, String endTime, String gameDate, String timeLeft) {
        this.timerID = timerID;
        this.roomID = roomID;
        this.roomName = roomName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gameDate = gameDate;
        this.timeLeft = timeLeft;
    }

    public int getTimerID() {
        return timerID;
    }
    public void setTimerID(int timerID) {
        this.timerID = timerID;
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

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGameDate() {
        return gameDate;
    }
    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getTimeLeft() {
        return timeLeft;
    }
    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }
}
