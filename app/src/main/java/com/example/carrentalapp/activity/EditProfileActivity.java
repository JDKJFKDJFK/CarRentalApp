package com.example.carrentalapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.db.AppDatabase;
import com.example.carrentalapp.databinding.ActivityEditProfileBinding;
import com.example.carrentalapp.utils.SessionManager;

import java.util.concurrent.Executors;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionManager session = new SessionManager(this);

        String uriStr = session.getProfileImage();
        if (uriStr != null && !uriStr.isEmpty()) {
            binding.imgProfileEdit.setImageURI(android.net.Uri.parse(uriStr));
        } else {
            binding.imgProfileEdit.setImageResource(R.mipmap.ic_launcher_round);
        }

        binding.imgProfileEdit.setOnClickListener(v -> pickImage.launch(new String[]{"image/*"}));

        binding.etName.setText(session.getName());
        binding.etEmail.setText(session.getEmail());
        binding.etPhone.setText(session.getPhone());

        binding.btnSave.setOnClickListener(v -> {

            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = session.getUserId();
            AppDatabase db = AppDatabase.getInstance(this);

            Executors.newSingleThreadExecutor().execute(() -> {
                if (userId != -1) {
                    db.userDao().updateProfile(userId, name, phone);
                }

                runOnUiThread(() -> {
                    session.saveProfile(name, email, phone);
                    Toast.makeText(this, getString(R.string.msg_profile_updated), Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }

    private android.net.Uri selectedImageUri;

    private final androidx.activity.result.ActivityResultLauncher<String[]> pickImage =
            registerForActivityResult(new androidx.activity.result.contract.ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri == null) return;
                        selectedImageUri = uri;

                        final int takeFlags = (android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        try {
                            getContentResolver().takePersistableUriPermission(uri, takeFlags);
                        } catch (Exception ignored) { }

                        binding.imgProfileEdit.setImageURI(uri);

                        SessionManager session = new SessionManager(this);
                        session.saveProfileImage(uri.toString());
                    });
}