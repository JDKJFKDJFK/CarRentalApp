package com.example.carrentalapp.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.carrentalapp.data.entity.BookingEntity;
import com.example.carrentalapp.data.entity.CarEntity;

public class BookingWithCar {

    @Embedded
    public BookingEntity booking;

    @Relation(
            parentColumn = "carId",
            entityColumn = "id"
    )
    public CarEntity car;
}
