package com.example.carrentalapp.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "favorites",
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
        indices = {
                @Index(value = {"carId"}),
                @Index(value = {"userId"}),
                @Index(value = {"carId", "userId"}, unique = true)
        }
)
public class FavoriteEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int carId;
    public int userId;

    public FavoriteEntity(int carId, int userId) {
        this.carId = carId;
        this.userId = userId;
    }
}
