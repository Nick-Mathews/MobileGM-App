package com.packages.mobilegamemasterapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.packages.mobilegamemasterapp.DAO.PasswordDAO;
import com.packages.mobilegamemasterapp.DAO.PuzzleDAO;
import com.packages.mobilegamemasterapp.DAO.RoomDAO;
import com.packages.mobilegamemasterapp.DAO.TimerDAO;
import com.packages.mobilegamemasterapp.Entities.Password;
import com.packages.mobilegamemasterapp.Entities.Puzzle;
import com.packages.mobilegamemasterapp.Entities.Room;
import com.packages.mobilegamemasterapp.Entities.Timer;

@Database(entities = {Room.class, Puzzle.class, Timer.class, Password.class}, version = 13, exportSchema = false)
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
