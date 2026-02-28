package com.example.carrentalapp.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "ratings",
        foreignKeys = {
                @ForeignKey(
                        entity = CarEntity.class,
                        parentColumns = "id",
                        childColumns = "carId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("carId"), @Index("userId")}
)
public class RatingEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int carId;
    public int userId;

    public int stars;
    public String comment;

    public long date;

    public RatingEntity(int carId, int userId,
                        int stars,
                        String comment,
                        long date) {

        this.carId = carId;
        this.userId = userId;
        this.stars = stars;
        this.comment = comment;
        this.date = date;
    }
}
