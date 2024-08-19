package com.packages.mobilegamemasterapp.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "password")
public class Password {

    @PrimaryKey(autoGenerate = true)
    private int passwordID;
    private String userName;
    private String password;

    public Password(int passwordID, String userName, String password) {
        this.passwordID = passwordID;
        this.userName = userName;
        this.password = password;
    }

    public int getPasswordID() {
        return passwordID;
    }

    public void setPasswordID(int passwordID) {
        this.passwordID = passwordID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
