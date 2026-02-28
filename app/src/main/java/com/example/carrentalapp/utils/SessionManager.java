package com.example.carrentalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String NAME = "session";
    private static final String KEY_ID = "user_id";

    private final SharedPreferences pref;


    public SessionManager(Context c) {

        pref = c.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
    }


    public void saveUserId(int id) {

        pref.edit().putInt(KEY_ID, id).apply();
    }


    public int getUserId() {

        return pref.getInt(KEY_ID, -1);
    }


    public void logout() {

        pref.edit().clear().apply();
    }


    public void saveProfile(String name, String email, String phone) {
        pref.edit()
                .putString("name", name)
                .putString("email", email)
                .putString("phone", phone)
                .apply();
    }

    public String getPhone() {
        return pref.getString("phone", "");
    }


    public String getName() {
        return pref.getString("name", "User");
    }

    public String getEmail() {
        return pref.getString("email", "user@email.com");
    }

    public boolean isLoggedIn() {

        return pref.getBoolean("login", false);
    }

    public void saveProfileImage(String uri) {
        pref.edit().putString("profile_image_uri", uri).apply();
    }

    public String getProfileImage() {
        return pref.getString("profile_image_uri", "");
    }




}
