package com.example.carrentalapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.R;
import com.example.carrentalapp.activity.CarDetailsActivity;
import com.example.carrentalapp.activity.FilterActivity;
import com.example.carrentalapp.adapter.CarAdapter;
import com.example.carrentalapp.adapter.CategoryAdapter;
import com.example.carrentalapp.adapter.FeaturedAdapter;
import com.example.carrentalapp.data.model.CarWithFav;
import com.example.carrentalapp.utils.SessionManager;
import com.example.carrentalapp.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel vm;

    private FeaturedAdapter featuredAdapter;
    private CarAdapter carsAdapter;

    private List<CarWithFav> lastAllCars = new ArrayList<>();
    private String selectedCategory = "All";

    private int maxPrice = Integer.MAX_VALUE;
    private int minYear = 0;
    private String transmission = "Any";
    private String fuel = "Any";
    private String seats = "Any";
    private boolean availableOnly = false;

    private ActivityResultLauncher<Intent> filterLauncher;

    private ImageView btnFilter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filterLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {

                        Intent data = result.getData();
                        maxPrice = data.getIntExtra("maxPrice", Integer.MAX_VALUE);
                        minYear = data.getIntExtra("minYear", 0);

                        transmission = data.getStringExtra("transmission");
                        fuel = data.getStringExtra("fuel");
                        seats = data.getStringExtra("seats");
                        availableOnly = data.getBooleanExtra("availableOnly", false);

                        if (transmission == null) transmission = "Any";
                        if (fuel == null) fuel = "Any";
                        if (seats == null) seats = "Any";

                        View v = getView();
                        if (v != null) {
                            EditText etSearch = v.findViewById(R.id.etSearch);
                            applyFilters(etSearch != null ? etSearch.getText().toString() : "");
                        } else {
                            applyFilters("");
                        }


                        Toast.makeText(requireContext(), getString(R.string.filter_applied), Toast.LENGTH_SHORT).show();
                        updateFilterIndicator();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        vm = new ViewModelProvider(this).get(HomeViewModel.class);

        SessionManager session = new SessionManager(requireContext());
        int tmpId = session.getUserId();
        final int userId = (tmpId == -1) ? 1 : tmpId;

        TextView tvUserName = view.findViewById(R.id.tvUserName);
        String name = session.getName();
        tvUserName.setText((name == null || name.trim().isEmpty()) ? getString(R.string.user) : name);

        RecyclerView recyclerFeatured = view.findViewById(R.id.recyclerFeatured);
        RecyclerView recyclerCategories = view.findViewById(R.id.recyclerCategories);
        RecyclerView recyclerCars = view.findViewById(R.id.recyclerCars);
        EditText etSearch = view.findViewById(R.id.etSearch);
        btnFilter = view.findViewById(R.id.btnFilter);

        if (btnFilter != null) {
            btnFilter.setOnClickListener(v ->
                    filterLauncher.launch(new Intent(requireContext(), FilterActivity.class))
            );
            updateFilterIndicator();
        }

        featuredAdapter = new FeaturedAdapter(item -> openDetails(item.car.id));
        recyclerFeatured.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerFeatured.setAdapter(featuredAdapter);

        List<String> cats = Arrays.asList("All", "Family", "SUV", "Economy", "Sport", "Electric", "Luxury");
        CategoryAdapter catAdapter = new CategoryAdapter(cats, cat -> {
            selectedCategory = cat;
            applyFilters(etSearch.getText().toString());
        });

        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCategories.setAdapter(catAdapter);

        carsAdapter = new CarAdapter(
                car -> openDetails(car.id),
                carId -> vm.toggleFavorite(userId, carId)
        );

        recyclerCars.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCars.setAdapter(carsAdapter);

        vm.getFeatured(userId).observe(getViewLifecycleOwner(), featuredAdapter::setList);

        vm.getCars(userId).observe(getViewLifecycleOwner(), list -> {
            lastAllCars = (list == null) ? new ArrayList<>() : list;
            applyFilters(etSearch.getText().toString());
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void applyFilters(String searchText) {
        String q = (searchText == null) ? "" : searchText.trim().toLowerCase();

        List<CarWithFav> filtered = new ArrayList<>();

        for (CarWithFav item : lastAllCars) {

            boolean okCategory = selectedCategory.equals("All") ||
                    item.car.category.equalsIgnoreCase(selectedCategory);

            boolean okSearch = q.isEmpty() ||
                    item.car.name.toLowerCase().contains(q);

            boolean okPrice = item.car.dailyPrice <= maxPrice;
            boolean okYear = item.car.year >= minYear;

            boolean okTransmission = "Any".equalsIgnoreCase(transmission) ||
                    item.car.transmission.equalsIgnoreCase(transmission);

            boolean okFuel = "Any".equalsIgnoreCase(fuel) ||
                    item.car.fuelType.equalsIgnoreCase(fuel);

            boolean okSeats = "Any".equalsIgnoreCase(seats) ||
                    item.car.seats == parseSeats(seats);

            boolean okAvailable = !availableOnly || item.car.available;

            if (okCategory && okSearch && okPrice && okYear && okTransmission && okFuel && okSeats && okAvailable) {
                filtered.add(item);
            }
        }

        carsAdapter.setCars(filtered);
    }

    private void updateFilterIndicator() {
        if (btnFilter == null) return;

        boolean anyFilter =
                maxPrice != Integer.MAX_VALUE ||
                        minYear != 0 ||
                        availableOnly ||
                        !"Any".equalsIgnoreCase(transmission) ||
                        !"Any".equalsIgnoreCase(fuel) ||
                        !"Any".equalsIgnoreCase(seats);

        btnFilter.setAlpha(anyFilter ? 1f : 0.5f);
    }

    private int parseSeats(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return -1; }
    }

    private void openDetails(int carId) {
        startActivity(new Intent(requireContext(), CarDetailsActivity.class)
                .putExtra("car_id", carId));
    }
}