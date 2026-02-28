package com.example.carrentalapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.carrentalapp.adapter.FavoriteAdapter;
import com.example.carrentalapp.databinding.FragmentFavoritesBinding;
import com.example.carrentalapp.utils.SessionManager;
import com.example.carrentalapp.viewmodel.FavoriteViewModel;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoriteViewModel vm;
    private FavoriteAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);


        vm = new ViewModelProvider(this)
                .get(FavoriteViewModel.class);


        SessionManager session =
                new SessionManager(requireContext());

        int userId = session.getUserId();


        adapter = new FavoriteAdapter(
                carId -> vm.remove(userId, carId),
                carId -> {
                    android.content.Intent i = new android.content.Intent(requireContext(), com.example.carrentalapp.activity.CarDetailsActivity.class);
                    i.putExtra("car_id", carId);
                    startActivity(i);
                }
        );

        binding.recyclerFavorites.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        binding.recyclerFavorites.setAdapter(adapter);


        vm.getFavorites(userId)
                .observe(getViewLifecycleOwner(), adapter::setList);


        return binding.getRoot();
    }
}
