package com.example.carrentalapp.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.db.AppDatabase;
import com.example.carrentalapp.data.entity.UserEntity;
import com.example.carrentalapp.databinding.ActivityRegisterBinding;

import java.util.concurrent.Executors;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);

        binding.btnRegister.setOnClickListener(v -> register());
    }

    private void register() {

        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String pass = binding.etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.msg_invalid_email), Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                UserEntity user = new UserEntity(name, email, phone, pass);
                db.userDao().insert(user);

                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.msg_registered), Toast.LENGTH_SHORT).show();
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, getString(R.string.msg_email_exists), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}