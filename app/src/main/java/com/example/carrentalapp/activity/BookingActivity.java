package com.example.carrentalapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.repository.CarRentalRepository;
import com.example.carrentalapp.databinding.ActivityBookingBinding;

import java.util.Calendar;

public class BookingActivity extends BaseActivity {

    private ActivityBookingBinding binding;
    private CarRentalRepository repo;

    private int carId;

    private long pickupTime = 0;
    private long returnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repo = new CarRentalRepository(getApplication());

        carId = getIntent().getIntExtra("car_id", -1);
        if (carId == -1) {
            Toast.makeText(this, getString(R.string.msg_invalid_car), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.btnPickupDate.setOnClickListener(v -> pickDateTime(true));
        binding.btnReturnDate.setOnClickListener(v -> pickDateTime(false));

        binding.btnConfirm.setOnClickListener(v -> {

            if (pickupTime == 0 || returnTime == 0) {
                Toast.makeText(this, getString(R.string.msg_select_pickup_return_first), Toast.LENGTH_SHORT).show();
                return;
            }

            if (returnTime <= pickupTime) {
                Toast.makeText(this, getString(R.string.msg_return_after_pickup), Toast.LENGTH_SHORT).show();
                return;
            }

            int days = calcDays(pickupTime, returnTime);

            repo.getCarById(carId).observe(this, car -> {
                if (car == null) return;

                double total = days * car.dailyPrice;

                Intent intent = new Intent(this, ConfirmBookingActivity.class);
                intent.putExtra("car_id", carId);
                intent.putExtra("pickup", pickupTime);
                intent.putExtra("return", returnTime);
                intent.putExtra("days", days);
                intent.putExtra("total", total);
                startActivity(intent);
            });
        });
    }

    private void pickDateTime(boolean isPickup) {
        Calendar now = Calendar.getInstance();

        DatePickerDialog dp = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    Calendar selected = Calendar.getInstance();
                    selected.set(Calendar.YEAR, year);
                    selected.set(Calendar.MONTH, month);
                    selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    new TimePickerDialog(
                            this,
                            (tp, hour, minute) -> {
                                selected.set(Calendar.HOUR_OF_DAY, hour);
                                selected.set(Calendar.MINUTE, minute);
                                selected.set(Calendar.SECOND, 0);

                                long time = selected.getTimeInMillis();

                                String text = dayOfMonth + "/" + (month + 1) + "/" + year +
                                        "  " + String.format("%02d:%02d", hour, minute);

                                if (isPickup) {
                                    pickupTime = time;
                                    binding.tvPickupDate.setText(text);
                                } else {
                                    returnTime = time;
                                    binding.tvReturnDate.setText(text);
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    ).show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dp.show();
    }

    private int calcDays(long start, long end) {
        long diff = end - start;
        double days = diff / (24.0 * 60 * 60 * 1000);
        int result = (int) Math.ceil(days);
        return Math.max(result, 1);
    }
}