package com.example.carrentalapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carrentalapp.data.entity.BookingEntity;
import com.example.carrentalapp.data.model.BookingWithCar;
import com.example.carrentalapp.data.repository.CarRentalRepository;

import java.util.List;

public class BookingViewModel extends AndroidViewModel {

    private final CarRentalRepository repo;

    public BookingViewModel(@NonNull Application app) {
        super(app);
        repo = new CarRentalRepository(app);
    }

    public LiveData<List<BookingWithCar>> getBookings(int userId) {
        return repo.getUserBookingsWithCar(userId);
    }

    public void createBooking(BookingEntity booking, OnResult callback) {
        new Thread(() -> repo.insertBooking(booking, () -> {
            if (callback != null) callback.onSuccess();
        })).start();
    }

    public void cancelBooking(int bookingId) {
        new Thread(() -> repo.cancelBooking(bookingId)).start();
    }

    public interface OnResult {
        void onSuccess();
        void onFail();
    }
}
