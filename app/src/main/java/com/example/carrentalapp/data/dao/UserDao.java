package com.example.carrentalapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.carrentalapp.data.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert
    void insert(UserEntity user);


    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity login(String email, String password);


    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    LiveData<UserEntity> getUserById(int id);


    @Query("UPDATE users SET name = :name, phone = :phone WHERE id = :id")
    void updateProfile(int id, String name, String phone);



}
