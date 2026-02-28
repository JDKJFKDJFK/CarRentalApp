package com.example.carrentalapp.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.carrentalapp.data.entity.CarEntity;
import com.example.carrentalapp.data.entity.FavoriteEntity;

public class FavoriteWithCar {

    @Embedded
    public FavoriteEntity favorite;

    @Relation(
            parentColumn = "carId",
            entityColumn = "id"
    )
    public CarEntity car;
}
