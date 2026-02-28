package com.example.carrentalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.carrentalapp.utils.LocaleHelper;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleHelper.apply(newBase));
    }
}