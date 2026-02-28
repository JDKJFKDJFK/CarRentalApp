package com.example.carrentalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.example.carrentalapp.R;
import com.example.carrentalapp.databinding.ActivityFilterBinding;

public class FilterActivity extends BaseActivity {

    private ActivityFilterBinding binding;

    private final String[] trKeys = {"Any", "Automatic", "Manual"};
    private final String[] fuelKeys = {"Any", "Petrol", "Diesel", "Hybrid", "Electric"};
    private final String[] seatsKeys = {"Any", "2", "4", "5", "7","8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] trLabels = {
                getString(R.string.any),
                getString(R.string.automatic),
                getString(R.string.manual)
        };

        String[] fuelLabels = {
                getString(R.string.any),
                getString(R.string.petrol),
                getString(R.string.diesel),
                getString(R.string.hybrid),
                getString(R.string.electric)
        };

        String[] seatsLabels = {
                getString(R.string.any),
                "2", "4", "5", "7","8"
        };

        binding.spTransmission.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, trLabels));

        binding.spFuel.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, fuelLabels));

        binding.spSeats.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, seatsLabels));

        binding.sbMaxPrice.setMax(500);
        binding.sbMaxPrice.setProgress(200);

        updateMaxPriceText(binding.sbMaxPrice.getProgress());

        binding.sbMaxPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateMaxPriceText(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.sbMinYear.setMax(30);
        binding.sbMinYear.setProgress(10);

        updateMinYearText(1995 + binding.sbMinYear.getProgress());

        binding.sbMinYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateMinYearText(1995 + progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.btnApply.setOnClickListener(v -> {
            int maxPrice = binding.sbMaxPrice.getProgress();
            int year = 1995 + binding.sbMinYear.getProgress();

            int trPos = binding.spTransmission.getSelectedItemPosition();
            int fuelPos = binding.spFuel.getSelectedItemPosition();
            int seatsPos = binding.spSeats.getSelectedItemPosition();

            String tr = trKeys[Math.max(0, trPos)];
            String fuelSel = fuelKeys[Math.max(0, fuelPos)];
            String seatsSel = seatsKeys[Math.max(0, seatsPos)];

            boolean onlyAvailable = binding.cbAvailable.isChecked();

            Intent data = new Intent();
            data.putExtra("maxPrice", maxPrice);
            data.putExtra("minYear", year);
            data.putExtra("transmission", tr);
            data.putExtra("fuel", fuelSel);
            data.putExtra("seats", seatsSel);
            data.putExtra("availableOnly", onlyAvailable);

            setResult(RESULT_OK, data);
            finish();
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.sbMaxPrice.setProgress(200);
            binding.sbMinYear.setProgress(10);
            binding.spTransmission.setSelection(0);
            binding.spFuel.setSelection(0);
            binding.spSeats.setSelection(0);
            binding.cbAvailable.setChecked(false);
        });
    }

    private void updateMaxPriceText(int price) {
        binding.tvMaxPrice.setText(getString(R.string.max_price_format, price));
    }

    private void updateMinYearText(int year) {
        binding.tvMinYear.setText(getString(R.string.min_year_format, year));
    }
}