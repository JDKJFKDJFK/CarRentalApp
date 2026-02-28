package com.example.carrentalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.carrentalapp.MainActivity;
import com.example.carrentalapp.R;
import com.example.carrentalapp.databinding.ActivityLoginBinding;
import com.example.carrentalapp.utils.SessionManager;
import com.example.carrentalapp.viewmodel.AuthViewModel;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    private AuthViewModel vm;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(AuthViewModel.class);
        session = new SessionManager(this);

        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_enter_email_password), Toast.LENGTH_SHORT).show();
                return;
            }

            vm.login(email, password).observe(this, user -> {
                if (user != null) {
                    session.saveUserId(user.id);
                    session.saveProfile(user.name, user.email, user.phone);

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.msg_invalid_email_password), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}