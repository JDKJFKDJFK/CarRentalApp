package com.example.carrentalapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.carrentalapp.data.entity.CarEntity;
import com.example.carrentalapp.data.repository.CarRentalRepository;

import java.util.List;

public class CarViewModel extends AndroidViewModel {

    private CarRentalRepository repo = null;

    private final MutableLiveData<String> query = new MutableLiveData<>("");

    private final LiveData<List<CarEntity>> cars = Transformations.switchMap(query, q -> {
        if (q == null || q.trim().isEmpty()) return repo.getAllCars();
        return repo.searchCars(q.trim());
    });

    public CarViewModel(@NonNull Application application) {
        super(application);
        repo = new CarRentalRepository(application);
    }

    public LiveData<List<CarEntity>> getCars() { return cars; }

    public void seed() { repo.seedCarsIfEmpty(); }

    public void setQuery(String q) { query.setValue(q); }
}
