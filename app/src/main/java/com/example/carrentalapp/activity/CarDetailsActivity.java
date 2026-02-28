package com.example.carrentalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.carrentalapp.R;
import com.example.carrentalapp.adapter.CarImagePagerAdapter;
import com.example.carrentalapp.data.repository.CarRentalRepository;
import com.example.carrentalapp.databinding.ActivityCarDetailsBinding;
import com.example.carrentalapp.utils.SessionManager;

import java.util.Locale;

public class CarDetailsActivity extends BaseActivity {

    private ActivityCarDetailsBinding binding;
    private int carId;
    private CarRentalRepository repo;

    private SessionManager session;
    private int userId = -1;

    private boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repo = new CarRentalRepository(getApplication());

        try {
            binding.btnBack.setOnClickListener(v -> finish());
        } catch (Exception ignored) { }

        carId = getIntent().getIntExtra("car_id", -1);
        if (carId == -1) {
            Toast.makeText(this, getString(R.string.msg_invalid_car), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        session = new SessionManager(this);
        userId = session.getUserId();

        if (userId != -1) {
            repo.isFavorite(carId, userId).observe(this, existsInt -> {
                isFav = existsInt != null && existsInt == 1;
                binding.btnFavorite.setImageResource(isFav ? R.drawable.ic_heart_fill : R.drawable.ic_heart_outline);
            });
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_heart_outline);
        }

        repo.getCarById(carId).observe(this, car -> {
            if (car == null) return;

            binding.tvName.setText(car.name);
            binding.tvCategory.setText(car.category);

            binding.tvSpecs.setText(getString(
                    R.string.specs_format,
                    car.year,
                    car.transmission,
                    car.seats,
                    car.fuelType
            ));

            binding.tvPrice.setText("$" + car.dailyPrice + " " + getString(R.string.per_day));

            try {
                binding.tvBottomPrice.setText(getString(R.string.starting_from_price, car.dailyPrice));
            } catch (Exception ignored) { }

            CarExtra extra = getCarExtra(car.name, car.imageRes);

            CarImagePagerAdapter pagerAdapter = new CarImagePagerAdapter(extra.images);
            binding.vpCarImages.setAdapter(pagerAdapter);

            updateCounter(0, extra.images.length);
            binding.vpCarImages.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    updateCounter(position, extra.images.length);
                }
            });

            binding.tvDescription.setText(extra.description);
            binding.tvFuelConsumption.setText(extra.consumption);

            binding.btnBookNow.setEnabled(car.available);
            binding.btnBookNow.setText(car.available ? getString(R.string.book_now) : getString(R.string.not_available));
        });

        repo.getAverageRating(carId).observe(this, avg -> {
            repo.getRatingsForCar(carId).observe(this, list -> {
                float a = (avg == null) ? 0f : avg;
                int c = (list == null) ? 0 : list.size();

                binding.tvRating.setText(getString(
                        R.string.rating_value_format,
                        String.format(Locale.getDefault(), "%.1f", a),
                        c
                ));
            });
        });

        binding.btnFavorite.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, getString(R.string.msg_please_login_first), Toast.LENGTH_SHORT).show();
                return;
            }

            binding.btnFavorite.setEnabled(false);

            if (isFav) {
                repo.removeFavorite(carId, userId, () -> runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.msg_removed_from_favorites), Toast.LENGTH_SHORT).show();
                    binding.btnFavorite.setEnabled(true);
                }));
            } else {
                repo.addFavorite(carId, userId, () -> runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.msg_added_to_favorites), Toast.LENGTH_SHORT).show();
                    binding.btnFavorite.setEnabled(true);
                }));
            }
        });

        binding.btnBookNow.setOnClickListener(v -> {
            repo.getCarById(carId).observe(this, car -> {
                if (car == null) return;

                if (!car.available) {
                    Toast.makeText(this, getString(R.string.msg_car_not_available_now), Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(this, BookingActivity.class).putExtra("car_id", carId));
            });
        });

        binding.btnRate.setOnClickListener(v ->
                startActivity(new Intent(this, RatingActivity.class).putExtra("car_id", carId))
        );
    }

    private void updateCounter(int index, int total) {
        binding.tvImageCounter.setText(getString(R.string.image_counter_format, index + 1, total));
    }

    private CarExtra getCarExtra(String carName, int mainImageRes) {
        int[] images = new int[]{mainImageRes};

        String desc = getString(R.string.desc_default);
        String consumption = getString(R.string.fuel_default);

        if (carName != null) {
            switch (carName) {
                case "Toyota Corolla":
                    desc = getString(R.string.desc_corolla);
                    consumption = getString(R.string.fuel_corolla);
                    break;

                case "Hyundai i10":
                    desc = getString(R.string.desc_hyundai_i10);
                    consumption = getString(R.string.fuel_hyundai_i10);
                    break;

                case "Kia Rio":
                    desc = getString(R.string.desc_kia_rio);
                    consumption = getString(R.string.fuel_kia_rio);
                    break;

                case "BMW X5":
                    desc = getString(R.string.desc_bmw_x5);
                    consumption = getString(R.string.fuel_bmw_x5);
                    break;

                case "Toyota Land Cruiser":
                    desc = getString(R.string.desc_land_cruiser);
                    consumption = getString(R.string.fuel_land_cruiser);
                    break;

                case "Nissan X-Trail":
                    desc = getString(R.string.desc_nissan_x_trail);
                    consumption = getString(R.string.fuel_nissan_x_trail);
                    break;

                case "Mercedes C200":
                    desc = getString(R.string.desc_mercedes_c200);
                    consumption = getString(R.string.fuel_mercedes_c200);
                    break;

                case "BMW 520i":
                    desc = getString(R.string.desc_bmw_520i);
                    consumption = getString(R.string.fuel_bmw_520i);
                    break;

                case "Audi A6":
                    desc = getString(R.string.desc_audi_a6);
                    consumption = getString(R.string.fuel_audi_a6);
                    break;

                case "Ford Mustang":
                    desc = getString(R.string.desc_ford_mustang);
                    consumption = getString(R.string.fuel_ford_mustang);
                    break;

                case "Chevrolet Camaro":
                    desc = getString(R.string.desc_chevrolet_camaro);
                    consumption = getString(R.string.fuel_chevrolet_camaro);
                    break;

                case "Porsche 911":
                    desc = getString(R.string.desc_porsche_911);
                    consumption = getString(R.string.fuel_porsche_911);
                    break;

                case "Tesla Model 3":
                    desc = getString(R.string.desc_tesla_model3);
                    consumption = getString(R.string.fuel_tesla_model3);
                    break;

                case "Nissan Leaf":
                    desc = getString(R.string.desc_nissan_leaf);
                    consumption = getString(R.string.fuel_nissan_leaf);
                    break;

                case "Hyundai Ioniq 5":
                    desc = getString(R.string.desc_hyundai_ioniq_5);
                    consumption = getString(R.string.fuel_hyundai_ioniq_5);
                    break;

                case "Toyota Sienna":
                    desc = getString(R.string.desc_toyota_sienna);
                    consumption = getString(R.string.fuel_toyota_sienna);
                    break;

                case "Kia Carnival":
                    desc = getString(R.string.desc_kia_carnival);
                    consumption = getString(R.string.fuel_kia_carnival);
                    break;

                case "Honda Odyssey":
                    desc = getString(R.string.desc_honda_odyssey);
                    consumption = getString(R.string.fuel_honda_odyssey);
                    break;
            }
        }

        return new CarExtra(images, desc, consumption);
    }

    private static class CarExtra {
        int[] images;
        String description;
        String consumption;

        CarExtra(int[] images, String description, String consumption) {
            this.images = images;
            this.description = description;
            this.consumption = consumption;
        }
    }
}