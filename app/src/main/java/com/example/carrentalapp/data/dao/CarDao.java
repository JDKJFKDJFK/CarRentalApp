package com.example.carrentalapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.carrentalapp.data.entity.CarEntity;
import com.example.carrentalapp.data.model.CarWithFav;

import java.util.List;

@Dao
public interface CarDao {

    @Insert
    void insert(CarEntity car);

    @Query("SELECT * FROM cars ORDER BY id DESC")
    LiveData<List<CarEntity>> getAllCars();

    @Query("SELECT * FROM cars WHERE id = :id LIMIT 1")
    LiveData<CarEntity> getCarById(int id);

    @Query("SELECT * FROM cars WHERE name LIKE '%' || :text || '%'")
    LiveData<List<CarEntity>> search(String text);

    @Query("SELECT COUNT(*) FROM cars")
    int countCars();

    @Query("UPDATE cars SET available = :available WHERE id = :id")
    void updateAvailability(int id, boolean available);



    // Cars + Favorite Status
    @Query(
            "SELECT c.*, " +
                    "CASE WHEN f.id IS NULL THEN 0 ELSE 1 END AS isFav " +
                    "FROM cars c " +
                    "LEFT JOIN favorites f " +
                    "ON c.id = f.carId AND f.userId = :userId " +
                    "ORDER BY c.id DESC"
    )
    LiveData<List<CarWithFav>> getCarsWithFav(int userId);


    // Featured Cars
    @Query(
            "SELECT c.*, " +
                    "CASE WHEN f.id IS NULL THEN 0 ELSE 1 END AS isFav " +
                    "FROM cars c " +
                    "LEFT JOIN favorites f " +
                    "ON c.id = f.carId AND f.userId = :userId " +
                    "ORDER BY c.id DESC " +
                    "LIMIT :limit"
    )
    LiveData<List<CarWithFav>> getFeaturedWithFav(int userId, int limit);
}
