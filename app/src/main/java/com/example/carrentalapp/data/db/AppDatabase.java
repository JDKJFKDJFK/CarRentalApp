package com.example.carrentalapp.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.carrentalapp.data.dao.BookingDao;
import com.example.carrentalapp.data.dao.CarDao;
import com.example.carrentalapp.data.dao.FavoriteDao;
import com.example.carrentalapp.data.dao.RatingDao;
import com.example.carrentalapp.data.dao.UserDao;
import com.example.carrentalapp.data.entity.BookingEntity;
import com.example.carrentalapp.data.entity.CarEntity;
import com.example.carrentalapp.data.entity.FavoriteEntity;
import com.example.carrentalapp.data.entity.RatingEntity;
import com.example.carrentalapp.data.entity.UserEntity;

@Database(
        entities = {
                CarEntity.class,
                UserEntity.class,
                BookingEntity.class,
                FavoriteEntity.class,
                RatingEntity.class
        },
        version = 2
)



public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract CarDao carDao();
    public abstract UserDao userDao();
    public abstract BookingDao bookingDao();
    public abstract FavoriteDao favoriteDao();
    public abstract RatingDao ratingDao();


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "car_rental_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
