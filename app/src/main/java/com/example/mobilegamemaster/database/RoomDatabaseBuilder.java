package com.example.mobilegamemaster.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mobilegamemaster.DAO.PuzzleDAO;
import com.example.mobilegamemaster.DAO.RoomDAO;
import com.example.mobilegamemaster.DAO.TimerDAO;
import com.example.mobilegamemaster.Entities.Puzzle;
import com.example.mobilegamemaster.Entities.Room;
import com.example.mobilegamemaster.Entities.Timer;

@Database(entities = {Room.class, Puzzle.class, Timer.class}, version = 10, exportSchema = false)
public abstract class RoomDatabaseBuilder extends RoomDatabase{

    //CREATE DAOS AND RDBB INSTANCE
    public abstract RoomDAO roomDAO();
    public abstract PuzzleDAO puzzleDAO();
    public abstract TimerDAO timerDAO();
    private static volatile RoomDatabaseBuilder Instance;

    //FUNCTION THAT GETS OR CREATES DB INSTANCE
    static RoomDatabaseBuilder getDatabase(final Context context) {
        if (Instance == null){
            synchronized (RoomDatabaseBuilder.class){
                if (Instance == null) {
                    Instance = androidx.room.Room.databaseBuilder(context.getApplicationContext(), RoomDatabaseBuilder.class,"MGMRoomDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return Instance;
    }
}
