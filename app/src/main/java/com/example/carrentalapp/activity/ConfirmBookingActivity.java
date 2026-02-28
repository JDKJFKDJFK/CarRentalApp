package com.example.carrentalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.carrentalapp.MainActivity;
import com.example.carrentalapp.R;
import com.example.carrentalapp.data.entity.BookingEntity;
import com.example.carrentalapp.databinding.ActivityConfirmBookingBinding;
import com.example.carrentalapp.utils.AlarmScheduler;
import com.example.carrentalapp.utils.NotificationUtils;
import com.example.carrentalapp.utils.SessionManager;
import com.example.carrentalapp.utils.SettingsManager;
import com.example.carrentalapp.viewmodel.BookingViewModel;

public class ConfirmBookingActivity extends BaseActivity {

    private ActivityConfirmBookingBinding binding;
    private BookingViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(BookingViewModel.class);

        SessionManager session = new SessionManager(this);
        int userId = session.getUserId();

        int carId = getIntent().getIntExtra("car_id", -1);
        long pickup = getIntent().getLongExtra("pickup", 0);
        long ret = getIntent().getLongExtra("return", 0);
        int days = getIntent().getIntExtra("days", 0);
        double total = getIntent().getDoubleExtra("total", 0);

        binding.tvDays.setText(getString(R.string.days_value_format, days));
        binding.tvTotal.setText(getString(R.string.total_value_format, total));

        binding.btnFinish.setOnClickListener(v -> {

            BookingEntity booking = new BookingEntity(
                    carId,
                    userId,
                    pickup,
                    ret,
                    days,
                    total,
                    "ACTIVE"
            );

            vm.createBooking(booking, new BookingViewModel.OnResult() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {

                        SettingsManager sm = new SettingsManager(ConfirmBookingActivity.this);
                        if (sm.notificationsEnabled()) {

                            long oneHour = 60 * 60 * 1000L;

                            long beforeEnd = ret - oneHour;
                            if (beforeEnd > System.currentTimeMillis()) {
                                AlarmScheduler.scheduleNotification(
                                        ConfirmBookingActivity.this,
                                        beforeEnd,
                                        (int) (System.currentTimeMillis() % 100000),
                                        getString(R.string.notif_booking_reminder_title),
                                        getString(R.string.notif_booking_reminder_body)
                                );
                            }

                            if (ret > System.currentTimeMillis()) {
                                AlarmScheduler.scheduleNotification(
                                        ConfirmBookingActivity.this,
                                        ret,
                                        (int) ((System.currentTimeMillis() + 1) % 100000),
                                        getString(R.string.notif_booking_ended_title),
                                        getString(R.string.notif_booking_ended_body)
                                );
                            }

                            long late = ret + oneHour;
                            AlarmScheduler.scheduleNotification(
                                    ConfirmBookingActivity.this,
                                    late,
                                    (int) ((System.currentTimeMillis() + 2) % 100000),
                                    getString(R.string.notif_late_return_title),
                                    getString(R.string.notif_late_return_body)
                            );
                        }

                        SettingsManager sm2 = new SettingsManager(ConfirmBookingActivity.this);
                        if (sm2.notificationsEnabled()) {
                            NotificationUtils.show(
                                    ConfirmBookingActivity.this,
                                    (int) (System.currentTimeMillis() % 100000),
                                    getString(R.string.notif_booking_success_title),
                                    getString(R.string.notif_booking_success_body)
                            );
                        }

                        Toast.makeText(ConfirmBookingActivity.this,
                                getString(R.string.msg_booking_confirmed),
                                Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ConfirmBookingActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    });
                }

                @Override
                public void onFail() {
                    runOnUiThread(() -> Toast.makeText(
                            ConfirmBookingActivity.this,
                            getString(R.string.msg_car_already_booked),
                            Toast.LENGTH_LONG
                    ).show());
                }
            });
        });
    }
}