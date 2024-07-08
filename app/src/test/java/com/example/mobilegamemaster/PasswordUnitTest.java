package com.example.mobilegamemaster;


import org.junit.Test;

import static org.junit.Assert.*;

import com.example.mobilegamemaster.Entities.Password;

public class PasswordUnitTest {
    Password password = new Password(1, "John", "1234");
    int correctID = 1;
    int incorrectID = -1;
    String correctName = "John";
    String correctPassword = "1234";
    String incorrectString = "";
    int getTestPasswordID = password.getPasswordID();
    String getTestUsername = password.getUserName();
    String getTestPassword = password.getPassword();

    //GET TESTS FOR PASSWORD ENTITY
    @Test
    public void pIDisCorrect() {assertEquals(correctID, getTestPasswordID);}
    @Test
    public void pIDisIncorrect() {assertNotEquals(incorrectID, getTestPasswordID);}
    @Test
    public void nameIsCorrect() {assertEquals(correctName, getTestUsername);}
    @Test
    public void nameIsIncorrect() {assertNotEquals(incorrectString, getTestUsername);}
    @Test
    public void passwordIsCorrect() {assertEquals(correctPassword, getTestPassword);}
    @Test
    public void passwordIsIncorrect() {assertNotEquals(incorrectString, getTestPassword);}

    //SET TESTS FOR PASSWORD ENTITY
    @Test
    public void setPasswordIDTest() {
        password.setPasswordID(2);
        assertEquals(2, password.getPasswordID());
    }
    @Test
    public void setNameTest() {
        password.setUserName("Jack");
        assertEquals("Jack", password.getUserName());
    }
    @Test
    public void setPasswordTest() {
        password.setPassword("4321");
        assertEquals("4321", password.getPassword());
    }
    @Test
    public void passIDIncorrect() {assertNotEquals(incorrectID, password.getPasswordID());}
    @Test
    public void uNameIncorrect() {assertNotEquals(incorrectString, password.getUserName());}
    @Test
    public void passIncorrect() {assertNotEquals(incorrectString, password.getPassword());}
}
