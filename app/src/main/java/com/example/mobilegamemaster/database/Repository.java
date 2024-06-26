package com.example.mobilegamemaster.database;

import android.app.Application;

import com.example.mobilegamemaster.DAO.PuzzleDAO;
import com.example.mobilegamemaster.DAO.RoomDAO;
import com.example.mobilegamemaster.DAO.TimerDAO;
import com.example.mobilegamemaster.Entities.Puzzle;
import com.example.mobilegamemaster.Entities.Room;
import com.example.mobilegamemaster.Entities.Timer;

import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final RoomDAO mRoomDAO;
    private final PuzzleDAO mPuzzleDAO;
    private final TimerDAO mTimerDAO;
    private List<Room> mAllRooms;
    private List<Puzzle> mAllPuzzles;
    private List<Puzzle> mRoomPuzzles;
    private List<Timer> mAllTimers;
    private Room mRoom;

    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        RoomDatabaseBuilder db = RoomDatabaseBuilder.getDatabase(application);
        mRoomDAO = db.roomDAO();
        mPuzzleDAO = db.puzzleDAO();
        mTimerDAO = db.timerDAO();
    }
    public List<Room> getmAllRooms()  {
        databaseExecutor.execute(()->{
            mAllRooms=mRoomDAO.getAllRooms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllRooms;
    }

    public Room getmRoom(int roomID){
        databaseExecutor.execute(()->{
            mRoom = mRoomDAO.getRoom(roomID);
        });
        try {
            Thread.sleep(1000);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return mRoom;
        }

    public void delete (Room room) throws InterruptedException {
        databaseExecutor.execute(()->{
            mRoomDAO.delete(room);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    public void insert (Room room) throws InterruptedException {
        databaseExecutor.execute(()->{
            mRoomDAO.insert(room);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    public void update (Room room) throws InterruptedException {
        databaseExecutor.execute(()->{
            mRoomDAO.update(room);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    public List<Puzzle> getmAllPuzzles(){
        databaseExecutor.execute(()->{
            mAllPuzzles=mPuzzleDAO.getAllPuzzles();

        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }

        return mAllPuzzles;

    }

    public List<Puzzle> getmRoomPuzzles(int roomID){
        databaseExecutor.execute(()->{
            mRoomPuzzles = mPuzzleDAO.getRoomPuzzles(roomID);
        });
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
        return mRoomPuzzles;
    }


    public void delete (Puzzle puzzle) throws InterruptedException {
        databaseExecutor.execute(()->{
            mPuzzleDAO.delete(puzzle);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    public void insert (Puzzle puzzle) throws InterruptedException{
        databaseExecutor.execute(()->{
            mPuzzleDAO.insert(puzzle);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    public void update (Puzzle puzzle) throws InterruptedException {
        databaseExecutor.execute(()->{
            mPuzzleDAO.update(puzzle);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }
    public List<Timer> getmAllTimers()  {
        databaseExecutor.execute(()->{
            mAllTimers=mTimerDAO.getAllTimers();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllTimers;
    }

    public void delete (Timer timer) throws InterruptedException {
        databaseExecutor.execute(()->{
            mTimerDAO.delete(timer);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    public void insert (Timer timer) throws InterruptedException{
        databaseExecutor.execute(()->{
            mTimerDAO.insert(timer);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    public void update (Timer timer) throws InterruptedException {
        databaseExecutor.execute(()->{
            mTimerDAO.update(timer);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }
}
