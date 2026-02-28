package com.example.carrentalapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carrentalapp.data.db.AppDatabase;
import com.example.carrentalapp.data.entity.FavoriteEntity;
import com.example.carrentalapp.data.model.CarWithFav;

import java.util.List;
import java.util.concurrent.Executors;

public class HomeViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public HomeViewModel(@NonNull Application app) {
        super(app);
        db = AppDatabase.getInstance(app);
    }

    public LiveData<List<CarWithFav>> getCars(int userId) {
        return db.carDao().getCarsWithFav(userId);
    }


    public LiveData<List<CarWithFav>> getFeatured(int userId) {
        return db.carDao().getFeaturedWithFav(userId, 6);
    }

    public void toggleFavorite(int userId, int carId) {
        Executors.newSingleThreadExecutor().execute(() -> {

            int count = db.favoriteDao().isFavoriteCount(carId, userId);
            boolean fav = count > 0;

            if (fav) {
                db.favoriteDao().delete(carId, userId);
            } else {
                db.favoriteDao().insert(new FavoriteEntity(carId, userId));
            }
        });
    }
}
