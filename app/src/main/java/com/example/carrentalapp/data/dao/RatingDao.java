package com.example.carrentalapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.carrentalapp.data.entity.RatingEntity;

import java.util.List;

@Dao
public interface RatingDao {

    @Insert
    void insert(RatingEntity rating);


    @Query("SELECT * FROM ratings WHERE carId = :carId")
    LiveData<List<RatingEntity>> getRatingsForCar(int carId);


    @Query("SELECT AVG(stars)\n" +
            "FROM ratings\n" +
            "WHERE carId = :carId")

    LiveData<Float> getAverage(int carId);
}
