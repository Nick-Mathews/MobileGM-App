package com.example.mobilegamemaster.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobilegamemaster.Entities.Timer;

import java.util.List;

@Dao
public interface TimerDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Timer timer);
    @Update
    void update(Timer timer);
    @Delete
    void delete(Timer timer);
    @Query("SELECT * FROM timers ORDER BY timerID ASC")
    List<Timer> getAllTimer();
}
