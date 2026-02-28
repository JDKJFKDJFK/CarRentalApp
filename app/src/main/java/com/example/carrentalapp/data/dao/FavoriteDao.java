package com.example.carrentalapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.carrentalapp.data.entity.FavoriteEntity;
import com.example.carrentalapp.data.model.FavoriteWithCar;

import java.util.List;

@Dao
public interface FavoriteDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavoriteEntity favorite);


    @Query("DELETE FROM favorites WHERE carId = :carId AND userId = :userId")
    void delete(int carId, int userId);


    @Query("SELECT * FROM favorites WHERE userId = :userId")
    LiveData<List<FavoriteEntity>> getFavoritesForUser(int userId);


    @Query("SELECT COUNT(*) FROM favorites WHERE carId = :carId AND userId = :userId")
    LiveData<Integer> isFavorite(int carId, int userId);

    @Query("SELECT COUNT(*) FROM favorites WHERE carId = :carId AND userId = :userId")
    int isFavoriteCount(int carId, int userId);


    @Transaction
    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<FavoriteWithCar>> getFavoritesWithCar(int userId);




}
