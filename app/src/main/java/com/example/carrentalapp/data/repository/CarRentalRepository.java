package com.example.carrentalapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.dao.BookingDao;
import com.example.carrentalapp.data.dao.CarDao;
import com.example.carrentalapp.data.dao.FavoriteDao;
import com.example.carrentalapp.data.dao.RatingDao;
import com.example.carrentalapp.data.dao.UserDao;
import com.example.carrentalapp.data.db.AppDatabase;
import com.example.carrentalapp.data.entity.BookingEntity;
import com.example.carrentalapp.data.entity.CarEntity;
import com.example.carrentalapp.data.entity.FavoriteEntity;
import com.example.carrentalapp.data.entity.RatingEntity;
import com.example.carrentalapp.data.entity.UserEntity;
import com.example.carrentalapp.data.model.BookingWithCar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarRentalRepository {

    private final CarDao carDao;
    private final UserDao userDao;
    private final BookingDao bookingDao;
    private final FavoriteDao favoriteDao;
    private final RatingDao ratingDao;

    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public CarRentalRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        carDao = db.carDao();
        userDao = db.userDao();
        bookingDao = db.bookingDao();
        favoriteDao = db.favoriteDao();
        ratingDao = db.ratingDao();
    }

    public LiveData<List<CarEntity>> getAllCars() {
        return carDao.getAllCars();
    }

    public LiveData<CarEntity> getCarById(int id) {
        return carDao.getCarById(id);
    }

    public LiveData<List<CarEntity>> searchCars(String text) {
        return carDao.search(text);
    }

    public void seedCarsIfEmpty() {
        io.execute(() -> {
            try {
                int count = carDao.countCars();
                if (count > 0) return;

                carDao.insert(new CarEntity("Toyota Corolla", "Economy", 50, true, 2021, "Automatic", 5, "Petrol", R.drawable.toyota_corolla));
                carDao.insert(new CarEntity("Hyundai i10", "Economy", 35, true, 2020, "Automatic", 4, "Petrol", R.drawable.hyundaii10));
                carDao.insert(new CarEntity("Kia Rio", "Economy", 45, true, 2019, "Manual", 5, "Petrol", R.drawable.kia_rio));

                carDao.insert(new CarEntity("BMW X5", "SUV", 120, true, 2022, "Automatic", 5, "Diesel", R.drawable.bmw_x5));
                carDao.insert(new CarEntity("Toyota Land Cruiser", "SUV", 160, true, 2021, "Automatic", 7, "Petrol", R.drawable.toyota_land_cruiser));
                carDao.insert(new CarEntity("Nissan X-Trail", "SUV", 95, true, 2020, "Automatic", 7, "Petrol", R.drawable.nissan_x_trail));

                carDao.insert(new CarEntity("Mercedes C200", "Luxury", 150, true, 2023, "Automatic", 5, "Petrol", R.drawable.mercedes_c200));
                carDao.insert(new CarEntity("BMW 520i", "Luxury", 170, true, 2022, "Automatic", 5, "Petrol", R.drawable.bmw_520i));
                carDao.insert(new CarEntity("Audi A6", "Luxury", 180, true, 2021, "Automatic", 5, "Petrol", R.drawable.audi_a6));

                carDao.insert(new CarEntity("Ford Mustang", "Sport", 200, true, 2021, "Automatic", 4, "Petrol", R.drawable.ford_mustang));
                carDao.insert(new CarEntity("Chevrolet Camaro", "Sport", 210, true, 2020, "Automatic", 4, "Petrol", R.drawable.chevrolet_camaro));
                carDao.insert(new CarEntity("Porsche 911", "Sport", 350, true, 2022, "Automatic", 2, "Petrol", R.drawable.porsche911));

                carDao.insert(new CarEntity("Tesla Model 3", "Electric", 180, true, 2023, "Automatic", 5, "Electric", R.drawable.tesla_model_3));
                carDao.insert(new CarEntity("Nissan Leaf", "Electric", 120, true, 2021, "Automatic", 5, "Electric", R.drawable.nissan_leaf));
                carDao.insert(new CarEntity("Hyundai Ioniq 5", "Electric", 160, true, 2022, "Automatic", 5, "Electric", R.drawable.hyundai_ioniq_5));

                carDao.insert(new CarEntity("Toyota Sienna", "Family", 110, true, 2021, "Automatic", 7, "Hybrid", R.drawable.toyota_sienna));
                carDao.insert(new CarEntity("Kia Carnival", "Family", 115, true, 2022, "Automatic", 8, "Petrol", R.drawable.kia_carnival));
                carDao.insert(new CarEntity("Honda Odyssey", "Family", 120, true, 2020, "Automatic", 8, "Petrol", R.drawable.honda_odyssey));
            } catch (Exception ignored) { }
        });
    }

    public void updateCarAvailability(int carId, boolean available) {
        io.execute(() -> carDao.updateAvailability(carId, available));
    }

    public LiveData<UserEntity> login(String email, String password) {
        MutableLiveData<UserEntity> liveData = new MutableLiveData<>();

        io.execute(() -> {
            UserEntity user = userDao.login(email, password);
            liveData.postValue(user);
        });

        return liveData;
    }

    public void register(UserEntity user, Runnable onDone, Runnable onEmailExists) {
        io.execute(() -> {
            try {
                userDao.insert(user);
                if (onDone != null) onDone.run();
            } catch (Exception e) {
                if (onEmailExists != null) onEmailExists.run();
            }
        });
    }

    public LiveData<UserEntity> getUserById(int id) {
        return userDao.getUserById(id);
    }

    public void updateProfile(int id, String name, String phone, Runnable onDone) {
        io.execute(() -> {
            userDao.updateProfile(id, name, phone);
            if (onDone != null) onDone.run();
        });
    }

    public LiveData<List<FavoriteEntity>> getFavoritesForUser(int userId) {
        return favoriteDao.getFavoritesForUser(userId);
    }

    public LiveData<Integer> isFavorite(int carId, int userId) {
        return favoriteDao.isFavorite(carId, userId);
    }

    public void addFavorite(int carId, int userId, Runnable onDone) {
        io.execute(() -> {
            favoriteDao.insert(new FavoriteEntity(carId, userId));
            if (onDone != null) onDone.run();
        });
    }

    public void removeFavorite(int carId, int userId, Runnable onDone) {
        io.execute(() -> {
            favoriteDao.delete(carId, userId);
            if (onDone != null) onDone.run();
        });
    }

    public LiveData<Float> getAverageRating(int carId) {
        return ratingDao.getAverage(carId);
    }

    public LiveData<List<RatingEntity>> getRatingsForCar(int carId) {
        return ratingDao.getRatingsForCar(carId);
    }

    public LiveData<List<BookingEntity>> getUserBookings(int userId) {
        return bookingDao.getUserBookings(userId);
    }

    public LiveData<List<BookingWithCar>> getUserBookingsWithCar(int userId) {
        return bookingDao.getBookingsWithCar(userId);
    }

    public void insertBooking(BookingEntity booking, Runnable onDone) {
        io.execute(() -> {
            bookingDao.insert(booking);
            carDao.updateAvailability(booking.carId, false);
            if (onDone != null) onDone.run();
        });
    }

    public void cancelBooking(int bookingId) {
        io.execute(() -> {
            int carId = bookingDao.getCarIdForBooking(bookingId);
            bookingDao.endBooking(bookingId);
            carDao.updateAvailability(carId, true);
        });
    }
}