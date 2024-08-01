package com.packages.mobilegamemaster.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.packages.mobilegamemaster.DAO.PasswordDAO;
import com.packages.mobilegamemaster.DAO.PuzzleDAO;
import com.packages.mobilegamemaster.DAO.RoomDAO;
import com.packages.mobilegamemaster.DAO.TimerDAO;
import com.packages.mobilegamemaster.Entities.Password;
import com.packages.mobilegamemaster.Entities.Puzzle;
import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.Entities.Timer;

@Database(entities = {Room.class, Puzzle.class, Timer.class, Password.class}, version = 12, exportSchema = false)
public abstract class RoomDatabaseBuilder extends RoomDatabase{

    //CREATE DAOS AND RDBB INSTANCE
    public abstract RoomDAO roomDAO();
    public abstract PuzzleDAO puzzleDAO();
    public abstract TimerDAO timerDAO();
    public abstract PasswordDAO passwordDAO();
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
