package com.example.carrentalapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.db.AppDatabase;
import com.example.carrentalapp.data.entity.RatingEntity;
import com.example.carrentalapp.databinding.ActivityRatingBinding;
import com.example.carrentalapp.utils.SessionManager;

import java.util.concurrent.Executors;

public class RatingActivity extends BaseActivity {

    private ActivityRatingBinding binding;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);

        int carId = getIntent().getIntExtra("car_id", -1);
        int userId = new SessionManager(this).getUserId();

        binding.btnSend.setOnClickListener(v -> {

            int stars = (int) binding.ratingBar.getRating();
            String comment = binding.etComment.getText().toString().trim();

            if (stars == 0) {
                Toast.makeText(this, getString(R.string.msg_select_stars_first), Toast.LENGTH_SHORT).show();
                return;
            }

            RatingEntity r = new RatingEntity(
                    carId,
                    userId,
                    stars,
                    comment,
                    System.currentTimeMillis()
            );

            Executors.newSingleThreadExecutor().execute(() -> {
                db.ratingDao().insert(r);

                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.msg_thanks_for_rating), Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}