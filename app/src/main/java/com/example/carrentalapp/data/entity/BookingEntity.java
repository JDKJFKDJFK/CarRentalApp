package com.example.carrentalapp.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "bookings",
        foreignKeys = {
                @ForeignKey(entity = CarEntity.class,
                        parentColumns = "id",
                        childColumns = "carId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("carId"), @Index("userId")}
)
public class BookingEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int carId;
    public int userId;

    public long pickupDateTime;
    public long returnDateTime;

    public int days;
    public double totalPrice;

    public String status;

    public BookingEntity(int carId, int userId, long pickupDateTime,
                         long returnDateTime, int days,
                         double totalPrice, String status) {
        this.carId = carId;
        this.userId = userId;
        this.pickupDateTime = pickupDateTime;
        this.returnDateTime = returnDateTime;
        this.days = days;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}
