package com.packages.mobilegamemaster;

import org.junit.Test;

import static org.junit.Assert.*;

import com.packages.mobilegamemaster.Entities.Timer;

public class TimerUnitTest {
    Timer timer = new Timer(1, 1, "Prison Break", "12:00 am", "12:59 am", "01/01/2024", "01:00");
    int correctNum = 1;
    int incorrectNum = -1;
    String correctName = "Prison Break";
    String correctStartTime = "12:00 am";
    String correctEndTime = "12:59 am";
    String correctDate = "01/01/2024";
    String correctTimeLeft = "01:00";
    String incorrectText = "";

    //GET TESTS FOR TIMER ENTITY
    @Test
    public void getTimerIDisCorrect() {assertEquals(correctNum, timer.getTimerID());}
    @Test
    public void timerIDisIncorrect() {assertNotEquals(incorrectNum, timer.getTimerID());}
    @Test
    public void getRoomIDisCorrect() {assertEquals(correctNum, timer.getRoomID());}
    @Test
    public void roomIDisIncorrect() {assertNotEquals(incorrectNum, timer.getRoomID());}
    @Test
    public void getRoomNameIsCorrect() {assertEquals(correctName, timer.getRoomName());}
    @Test
    public void roomNameIsIncorrect() {assertNotEquals(incorrectText, timer.getRoomName());}
    @Test
    public void getStartTimeIsCorrect() {assertEquals(correctStartTime, timer.getStartTime());}
    @Test
    public void startTimeIsIncorrect() {assertNotEquals(incorrectText, timer.getStartTime());}
    @Test
    public void getEndTimeIsCorrect() {assertEquals(correctEndTime, timer.getEndTime());}
    @Test
    public void endTimeIsIncorrect() {assertNotEquals(incorrectText, timer.getEndTime());}
    @Test
    public void getGameDateIsCorrect() {assertEquals(correctDate, timer.getGameDate());}
    @Test
    public void gameDateIsIncorrect() {assertNotEquals(incorrectText, timer.getGameDate());}
    @Test
    public void getTimeLeftIsCorrect() {assertEquals(correctTimeLeft, timer.getTimeLeft());}
    @Test
    public void timeLeftIsIncorrect() {assertNotEquals(incorrectText, timer.getTimeLeft());}

    //SET TESTS FOR TIMER ENTITY
    @Test
    public void setTimerIDisCorrect() {
        timer.setTimerID(2);
        assertEquals(2, timer.getTimerID());
    }
    @Test
    public void timerIDIncorrect() {assertNotEquals(incorrectNum, timer.getTimerID());}
    @Test
    public void setRoomIDisCorrect() {
        timer.setRoomID(2);
        assertEquals(2, timer.getRoomID());
    }
    @Test
    public void roomIDIncorrect() {assertNotEquals(incorrectNum, timer.getRoomID());}
    @Test
    public void setRoomNameIsCorrect() {
        timer.setRoomName("Ivans Lair");
        assertEquals("Ivans Lair", timer.getRoomName());
    }
    @Test
    public void roomNameIncorrect() {assertNotEquals(incorrectText, timer.getRoomName());}
    @Test
    public void setStartTimeIsCorrect() {
        timer.setStartTime("01:00 am");
        assertEquals("01:00 am", timer.getStartTime());
    }
    @Test
    public void startTimeIncorrect() {assertNotEquals(incorrectText, timer.getStartTime());}
    @Test
    public void setEndTimeIsCorrect() {
        timer.setEndTime("01:58 am");
        assertEquals("01:58 am", timer.getEndTime());
    }
    @Test
    public void endTimeIncorrect() {assertNotEquals(incorrectText, timer.getEndTime());}
    @Test
    public void setGameDateIsCorrect() {
        timer.setGameDate("01/02/2024");
        assertEquals("01/02/2024", timer.getGameDate());
    }
    @Test
    public void gameDateIncorrect() {assertNotEquals(incorrectText, timer.getGameDate());}
    @Test
    public void setTimeLeftIsCorrect() {
        timer.setTimeLeft("02:00");
        assertEquals("02:00", timer.getTimeLeft());
    }
    @Test
    public void timeLeftIncorrect() {assertNotEquals(incorrectText, timer.getTimeLeft());}
}
