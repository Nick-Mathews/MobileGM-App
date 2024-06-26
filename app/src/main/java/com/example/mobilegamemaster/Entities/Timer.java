package com.example.mobilegamemaster.Entities;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mobilegamemaster.UI.RoomLoss;
import com.example.mobilegamemaster.UI.RoomPuzzles;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName="timers")
public class Timer {
    @PrimaryKey(autoGenerate = true)
    int timerID;
    int roomID;
    String endTime;
    String gameDate;
    String timeLeft;

    public Timer(int timerID, int roomID, String endTime, String gameDate, String timeLeft) {
        this.timerID = timerID;
        this.roomID = roomID;
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
