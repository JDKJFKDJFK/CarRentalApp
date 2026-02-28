package com.example.carrentalapp;

import android.app.Application;
import android.content.Context;

import com.example.carrentalapp.utils.LocaleHelper;

public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.apply(base));
    }
}