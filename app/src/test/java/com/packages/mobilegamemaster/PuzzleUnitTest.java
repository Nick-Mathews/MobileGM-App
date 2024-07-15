package com.packages.mobilegamemaster;

import org.junit.Test;

import static org.junit.Assert.*;

import com.packages.mobilegamemaster.Entities.Puzzle;

public class PuzzleUnitTest {
    Puzzle puzzle = new Puzzle(1, 1, 1, "Nudge", "Hint", "Solution");
    int correctNum = 1;
    int incorrectNum = -1;
    String correctNudge = "Nudge";
    String correctHint = "Hint";
    String correctSolution = "Solution";
    String incorrectText = "";
    int getTestPuzzleID = puzzle.getPuzzleID();
    int getTestPuzzleNum = puzzle.getPuzzleNum();
    int getTestRoomID = puzzle.getRoomID();
    String getTestNudge = puzzle.getNudge();
    String getTestHint = puzzle.getHint();
    String getTestSolution = puzzle.getSolution();

    //GET TESTS FOR PUZZLE ENTITY
    @Test
    public void pIDisCorrect() {assertEquals(correctNum, getTestPuzzleID);}
    @Test
    public void puzzleIDisIncorrect() {assertNotEquals(incorrectNum, getTestPuzzleID);}
    @Test
    public void puzzleNumIsCorrect() {assertEquals(correctNum, getTestPuzzleNum);}
    @Test
    public void puzzleNumIsIncorrect() {assertNotEquals(incorrectNum, getTestPuzzleNum);}
    @Test
    public void roomIDisCorrect() {assertEquals(correctNum, getTestRoomID);}
    @Test
    public void roomIDisIncorrect() {assertNotEquals(incorrectNum, getTestRoomID);}
    @Test
    public void nudgeIsCorrect() {assertEquals(correctNudge, getTestNudge);}
    @Test
    public void nudgeIsIncorrect() {assertNotEquals(incorrectText, getTestNudge);}
    @Test
    public void hintIsCorrect() {assertEquals(correctHint, getTestHint);}
    @Test
    public void hintIsIncorrect() {assertNotEquals(incorrectText, getTestHint);}
    @Test
    public void solutionIsCorrect() {assertEquals(correctSolution, getTestSolution);}
    @Test
    public void solutionIsIncorrect() {assertNotEquals(incorrectText, getTestSolution);}
    //SET TESTS FOR ENTITY
    @Test
    public void setPuzzleIDTest() {
        puzzle.setPuzzleID(2);
        assertEquals(2, puzzle.getPuzzleID());
    }
    @Test
    public void setPuzzleNumTest() {
        puzzle.setPuzzleNum(2);
        assertEquals(2, puzzle.getPuzzleNum());
    }
    @Test
    public void setRoomIDTest() {
        puzzle.setRoomID(2);
        assertEquals(2, puzzle.getRoomID());
    }
    @Test
    public void setNudgeTest() {
        puzzle.setNudge("N");
        assertEquals("N", puzzle.getNudge());
    }
    @Test
    public void setHintTest() {
        puzzle.setHint("H");
        assertEquals("H", puzzle.getHint());
    }
    @Test
    public void setSolutionTest() {
        puzzle.setSolution("S");
        assertEquals("S", puzzle.getSolution());
    }
    @Test
    public void puzzleIDIsIncorrect() {
        assertNotEquals(incorrectNum, puzzle.getPuzzleID());
    }
    @Test
    public void puzzleNumIncorrect() {
        assertNotEquals(incorrectNum, puzzle.getPuzzleNum());
    }
    @Test
    public void roomIDIncorrect() {
        assertNotEquals(incorrectNum, puzzle.getRoomID());
    }
    @Test
    public void nudgeIncorrect() {
        assertNotEquals(incorrectText, puzzle.getNudge());
    }
    @Test
    public void hintIncorrect() {
        assertNotEquals(incorrectText, puzzle.getHint());
    }
    @Test
    public void solutionIncorrect() {
        assertNotEquals(incorrectText, puzzle.getSolution());
    }
}
