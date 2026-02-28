package com.example.carrentalapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carrentalapp.data.entity.UserEntity;
import com.example.carrentalapp.data.repository.CarRentalRepository;

public class AuthViewModel extends AndroidViewModel {

    private final CarRentalRepository repo;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repo = new CarRentalRepository(application);
    }

    public LiveData<UserEntity> login(String email, String password) {
        return repo.login(email, password);
    }

    public void register(UserEntity user, Runnable onDone, Runnable onEmailExists) {
        repo.register(user, onDone, onEmailExists);
    }
}
