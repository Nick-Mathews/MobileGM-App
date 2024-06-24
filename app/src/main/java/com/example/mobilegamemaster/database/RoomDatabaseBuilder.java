package com.example.mobilegamemaster.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mobilegamemaster.DAO.PuzzleDAO;
import com.example.mobilegamemaster.DAO.RoomDAO;
import com.example.mobilegamemaster.Entities.Puzzle;
import com.example.mobilegamemaster.Entities.Room;

@Database(entities = {Room.class, Puzzle.class}, version = 3, exportSchema = false)
public abstract class RoomDatabaseBuilder extends RoomDatabase{

    public abstract RoomDAO roomDAO();
    public abstract PuzzleDAO puzzleDAO();
    private static volatile RoomDatabaseBuilder Instance;

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
