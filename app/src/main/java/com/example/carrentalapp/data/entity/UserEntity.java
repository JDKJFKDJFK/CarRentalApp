package com.example.carrentalapp.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        indices = {@Index(value = {"email"}, unique = true)}
)
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String email;
    public String phone;
    public String password;

    public UserEntity(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}
