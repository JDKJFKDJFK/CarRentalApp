package com.example.carrentalapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.carrentalapp.MainActivity;
import com.example.carrentalapp.R;
import com.example.carrentalapp.databinding.ActivitySettingsBinding;
import com.example.carrentalapp.utils.SessionManager;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = getSharedPreferences("settings", MODE_PRIVATE);

        loadSettings();

        binding.switchNotifications.setOnCheckedChangeListener(
                (b, checked) -> pref.edit().putBoolean("notif", checked).apply()
        );

        binding.switchDark.setOnCheckedChangeListener((b, checked) -> {

            pref.edit().putBoolean("dark", checked).apply();

            if (checked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        binding.btnEditProfile.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class))
        );

        binding.btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void loadSettings() {

        boolean notif = pref.getBoolean("notif", true);
        boolean dark = pref.getBoolean("dark", false);

        binding.switchNotifications.setChecked(notif);
        binding.switchDark.setChecked(dark);

        String lang = pref.getString("lang", "en");
        if ("ar".equals(lang)) binding.rbAr.setChecked(true);
        else binding.rbEn.setChecked(true);

        binding.radioLang.setOnCheckedChangeListener(null);

        binding.radioLang.setOnCheckedChangeListener((group, checkedId) -> {
            String newLang = (checkedId == binding.rbAr.getId()) ? "ar" : "en";

            String current = pref.getString("lang", "en");
            if (newLang.equals(current)) return;

            pref.edit().putString("lang", newLang).apply();

            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout_confirm_title))
                .setMessage(getString(R.string.logout_confirm_msg))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> logout())
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void logout() {

        new SessionManager(this).logout();

        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}