package com.example.mobilegamemaster.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobilegamemaster.Entities.Room;

import java.util.List;

@Dao
public interface RoomDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Room room);
    @Update
    void update(Room room);
    @Delete
    void delete(Room room);
    @Query("SELECT * FROM ROOMS ORDER BY roomID ASC")
    List<Room> getAllRooms();
    @Query("SELECT * FROM ROOMS WHERE roomID = :roomID")
    Room getRoom(int roomID);
}
