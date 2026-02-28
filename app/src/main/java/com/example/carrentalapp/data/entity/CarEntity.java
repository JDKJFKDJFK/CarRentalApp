package com.example.carrentalapp.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "cars",
        indices = {@Index(value = {"name"}), @Index(value = {"category"})}
)
public class CarEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String category;
    public double dailyPrice;
    public boolean available;
    public int year;
    public String transmission;
    public int seats;
    public String fuelType;

    public int imageRes;

    public CarEntity(String name, String category, double dailyPrice,
                     boolean available, int year, String transmission,
                     int seats, String fuelType, int imageRes) {
        this.name = name;
        this.category = category;
        this.dailyPrice = dailyPrice;
        this.available = available;
        this.year = year;
        this.transmission = transmission;
        this.seats = seats;
        this.fuelType = fuelType;
        this.imageRes = imageRes;
    }
}
