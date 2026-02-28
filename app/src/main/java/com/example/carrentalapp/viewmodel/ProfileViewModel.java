package com.example.carrentalapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carrentalapp.data.db.AppDatabase;
import com.example.carrentalapp.data.entity.UserEntity;

import java.util.concurrent.Executors;

public class ProfileViewModel extends AndroidViewModel {

    private final AppDatabase db;


    public ProfileViewModel(@NonNull Application app) {
        super(app);
        db = AppDatabase.getInstance(app);
    }


    public LiveData<UserEntity> getUser(int id) {
        return db.userDao().getUserById(id);
    }


    public void update(int id, String name, String phone) {

        Executors.newSingleThreadExecutor().execute(() ->
                db.userDao().updateProfile(id, name, phone)
        );
    }
}
