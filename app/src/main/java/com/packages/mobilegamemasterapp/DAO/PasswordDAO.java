package com.packages.mobilegamemasterapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.packages.mobilegamemasterapp.Entities.Password;

import java.util.List;

@Dao
public interface PasswordDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Password password);

    @Update
    void update(Password password);

    @Delete
    void delete(Password password);

    @Query("SELECT * FROM password WHERE :passwordID == passwordID ORDER BY passwordID ASC")
    Password getPassword(int passwordID);

    @Query("SELECT * FROM password ORDER BY passwordID ASC ")
    List<Password> getAllPasswords();
}
