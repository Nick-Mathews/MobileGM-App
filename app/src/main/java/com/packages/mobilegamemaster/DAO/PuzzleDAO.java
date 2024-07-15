package com.packages.mobilegamemaster.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.packages.mobilegamemaster.Entities.Puzzle;

import java.util.List;

@Dao
public interface PuzzleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Puzzle puzzle);

    @Update
    void update(Puzzle puzzle);

    @Delete
    void delete(Puzzle puzzle);

    @Query("SELECT * FROM puzzles ORDER BY puzzleID ASC")
    List<Puzzle> getAllPuzzles();

    @Query("SELECT * FROM puzzles WHERE roomID == :roomID ORDER BY puzzleNum ASC")
    List<Puzzle> getRoomPuzzles(int roomID);

    @Query("SELECT * FROM puzzles WHERE puzzleID == :puzzleID ORDER BY puzzleNum ASC")
    Puzzle getPuzzle(int puzzleID);
}
