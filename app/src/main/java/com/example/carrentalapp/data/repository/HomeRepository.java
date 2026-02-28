package com.example.carrentalapp.data.repository;

import androidx.lifecycle.LiveData;

import com.example.carrentalapp.data.dao.CarDao;
import com.example.carrentalapp.data.dao.FavoriteDao;
import com.example.carrentalapp.data.entity.FavoriteEntity;
import com.example.carrentalapp.data.model.CarWithFav;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeRepository {

    private final CarDao carDao;
    private final FavoriteDao favoriteDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public HomeRepository(CarDao carDao, FavoriteDao favoriteDao) {
        this.carDao = carDao;
        this.favoriteDao = favoriteDao;
    }

    public LiveData<List<CarWithFav>> getCars(int userId) {
        return carDao.getCarsWithFav(userId);
    }

    public LiveData<List<CarWithFav>> getFeatured(int userId) {
        return carDao.getFeaturedWithFav(userId, 5);
    }

    public void toggleFavorite(int userId, int carId) {
        executor.execute(() -> {

            int count = favoriteDao.isFavoriteCount(carId, userId);
            boolean isFav = count > 0;

            if (isFav) {
                favoriteDao.delete(carId, userId);
            } else {
                favoriteDao.insert(new FavoriteEntity(carId, userId));
            }
        });
    }
}
