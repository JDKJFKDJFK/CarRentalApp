package com.example.carrentalapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carrentalapp.data.db.AppDatabase;
import com.example.carrentalapp.data.model.FavoriteWithCar;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private final AppDatabase db;


    public FavoriteViewModel(@NonNull Application app) {
        super(app);

        db = AppDatabase.getInstance(app);
    }


    public LiveData<List<FavoriteWithCar>> getFavorites(int userId) {

        return db.favoriteDao()
                .getFavoritesWithCar(userId);
    }


    public void remove(int userId, int carId) {

        new Thread(() ->

                db.favoriteDao()
                        .delete(carId, userId)

        ).start();
    }
}
