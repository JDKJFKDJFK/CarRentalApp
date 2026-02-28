package com.example.carrentalapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

import com.example.carrentalapp.data.entity.CarEntity;

public class CarWithFav {

    @Embedded
    public CarEntity car;

    @ColumnInfo(name = "isFav")
    public boolean isFav;
}
