package com.example.carrentalapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.carrentalapp.data.entity.BookingEntity;
import com.example.carrentalapp.data.model.BookingWithCar;

import java.util.List;

@Dao
public interface BookingDao {

    @Insert
    long insert(BookingEntity booking);


    @Query("SELECT * FROM bookings " +
            "WHERE userId = :userId " +
            "ORDER BY pickupDateTime DESC")
    LiveData<List<BookingEntity>> getUserBookings(int userId);


    @Query("SELECT COUNT(*) FROM bookings " +
            "WHERE carId = :carId " +
            "AND status = 'ACTIVE' " +
            "AND ( " +
            "(:pickup BETWEEN pickupDateTime AND returnDateTime) " +
            "OR (:returnDate BETWEEN pickupDateTime AND returnDateTime) " +
            "OR (pickupDateTime BETWEEN :pickup AND :returnDate) " +
            ")")
    int isCarBooked(int carId, long pickup, long returnDate);


    @Query("UPDATE bookings SET status = 'ENDED' WHERE id = :bookingId")
    void endBooking(int bookingId);

    @Query("SELECT carId FROM bookings WHERE id = :bookingId LIMIT 1")
    int getCarIdForBooking(int bookingId);


    @Transaction
    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<BookingWithCar>> getBookingsWithCar(int userId);






}
