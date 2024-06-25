package com.example.mobilegamemaster.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "puzzles")
public class Puzzle {
    @PrimaryKey(autoGenerate = true)
    private int puzzleID;
    private int puzzleNum;
    private int roomID;
    private String nudge;
    private String hint;
    private String solution;


    public Puzzle(int puzzleID, int puzzleNum, int roomID, String nudge, String hint, String solution) {
        this.puzzleID = puzzleID;
        this.puzzleNum = puzzleNum;
        this.roomID = roomID;
        this.nudge = nudge;
        this.hint = hint;
        this.solution = solution;
    }

    public int getPuzzleID() {
        return puzzleID;
    }
    public void setPuzzleID(int puzzleId) {
        this.puzzleID = puzzleId;
    }
    public int getPuzzleNum() {
        return puzzleNum;
    }
    public void setPuzzleNum(int puzzleNum) {
        this.puzzleNum = puzzleNum;
    }
    public int getRoomID() {
        return roomID;
    }
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    public String getNudge() {
        return nudge;
    }
    public void setNudge(String nudge) {
        this.nudge = nudge;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public String getSolution() {
        return solution;
    }
    public void setSolution(String solution) {
        this.solution = solution;
    }
}
