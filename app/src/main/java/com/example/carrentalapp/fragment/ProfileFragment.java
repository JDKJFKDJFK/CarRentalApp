package com.example.carrentalapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.carrentalapp.R;
import com.example.carrentalapp.activity.EditProfileActivity;
import com.example.carrentalapp.activity.MyBookingsActivity;
import com.example.carrentalapp.activity.SettingsActivity;
import com.example.carrentalapp.data.model.BookingWithCar;
import com.example.carrentalapp.databinding.FragmentProfileBinding;
import com.example.carrentalapp.utils.SessionManager;
import com.example.carrentalapp.viewmodel.BookingViewModel;

import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        SessionManager session = new SessionManager(requireContext());

        String uriStr = session.getProfileImage();
        if (uriStr != null && !uriStr.isEmpty()) {
            binding.imgProfile.setImageURI(android.net.Uri.parse(uriStr));
        } else {
            binding.imgProfile.setImageResource(R.mipmap.ic_launcher_round);
        }

        binding.tvName.setText(session.getName());
        binding.tvEmail.setText(session.getEmail());
        binding.tvPhone.setText(session.getPhone());

        binding.btnEdit.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), EditProfileActivity.class))
        );

        binding.btnMyBookings.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), MyBookingsActivity.class))
        );

        binding.btnSettings.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), SettingsActivity.class))
        );

        int userId = session.getUserId();
        if (userId != -1) {
            BookingViewModel vm = new ViewModelProvider(this).get(BookingViewModel.class);
            vm.getBookings(userId).observe(getViewLifecycleOwner(), (List<BookingWithCar> list) -> {
                int total = (list == null) ? 0 : list.size();
                int active = 0;

                if (list != null) {
                    for (BookingWithCar b : list) {
                        if ("ACTIVE".equals(b.booking.status)) active++;
                    }
                }

                binding.tvTotalBookings.setText(String.valueOf(total));
                binding.tvActiveBookings.setText(String.valueOf(active));
            });
        } else {
            binding.tvTotalBookings.setText("0");
            binding.tvActiveBookings.setText("0");
        }

        return binding.getRoot();
    }
}