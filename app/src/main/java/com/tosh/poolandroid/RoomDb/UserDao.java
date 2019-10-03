package com.tosh.poolandroid.RoomDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tosh.poolandroid.Model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(UserRoom user);

    @Delete
    void delete(UserRoom user);

    @Query("SELECT * FROM user_table ORDER BY email")
    LiveData<List<UserRoom>>  getUserDetails();


}
