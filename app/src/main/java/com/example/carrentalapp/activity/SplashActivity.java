package com.example.carrentalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.carrentalapp.MainActivity;
import com.example.carrentalapp.R;
import com.example.carrentalapp.utils.SessionManager;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            boolean done = getSharedPreferences("settings", MODE_PRIVATE)
                    .getBoolean("onboarding_done", false);

            if (!done) {
                startActivity(new Intent(this, OnboardingActivity.class));
                finish();
                return;
            }

            SessionManager session = new SessionManager(this);
            boolean isLogin = session.getUserId() != -1;

            if (isLogin) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();

        }, 2000);
    }
}