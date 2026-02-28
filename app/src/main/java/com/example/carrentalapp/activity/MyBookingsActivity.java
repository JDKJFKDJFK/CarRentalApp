package com.example.carrentalapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.carrentalapp.R;
import com.example.carrentalapp.adapter.BookingAdapter;
import com.example.carrentalapp.data.model.BookingWithCar;
import com.example.carrentalapp.databinding.ActivityMyBookingsBinding;
import com.example.carrentalapp.utils.SessionManager;
import com.example.carrentalapp.viewmodel.BookingViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends BaseActivity {

    private ActivityMyBookingsBinding binding;
    private BookingViewModel vm;
    private BookingAdapter adapter;

    private List<BookingWithCar> all = new ArrayList<>();
    private boolean showingActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(BookingViewModel.class);

        SessionManager session = new SessionManager(this);
        int userId = session.getUserId();

        if (userId == -1) {
            Toast.makeText(this, getString(R.string.msg_please_login_first), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new BookingAdapter(this::showCancelDialog);



        binding.recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerBookings.setAdapter(adapter);

        binding.btnActive.setOnClickListener(v -> {
            showingActive = true;
            applyFilter();
        });

        binding.btnPast.setOnClickListener(v -> {
            showingActive = false;
            applyFilter();
        });

        vm.getBookings(userId).observe(this, list -> {
            all = (list == null) ? new ArrayList<>() : list;

            long now = System.currentTimeMillis();
            for (BookingWithCar b : all) {
                if ("ACTIVE".equals(b.booking.status) && b.booking.returnDateTime < now) {
                    vm.cancelBooking(b.booking.id);
                }
            }

            applyFilter();
        });

        applyFilter();
    }

    private void applyFilter() {
        List<BookingWithCar> filtered = new ArrayList<>();

        for (BookingWithCar b : all) {
            boolean isActive = "ACTIVE".equals(b.booking.status);

            if (showingActive && isActive) filtered.add(b);
            if (!showingActive && !isActive) filtered.add(b);
        }

        adapter.setList(filtered);

        binding.tvCount.setText(getString(R.string.showing_count_format, filtered.size()));

        binding.tvEmpty.setVisibility(filtered.isEmpty()
                ? android.view.View.VISIBLE
                : android.view.View.GONE);
    }

    private void showCancelDialog(int bookingId) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.cancel_booking_title))
                .setMessage(getString(R.string.cancel_booking_msg))
                .setPositiveButton(getString(R.string.yes), (d, w) -> vm.cancelBooking(bookingId))
                .setNegativeButton(getString(R.string.no), (d, w) -> d.dismiss())
                .show();
    }
}