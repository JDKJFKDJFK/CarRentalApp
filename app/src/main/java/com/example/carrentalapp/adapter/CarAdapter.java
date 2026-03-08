package com.example.carrentalapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.entity.CarEntity;
import com.example.carrentalapp.data.model.CarWithFav;
import com.example.carrentalapp.databinding.ItemCarBinding;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<CarWithFav> cars = new ArrayList<>();

    private final OnCarClickListener listener;
    private final OnFavClickListener favListener;

    public interface OnCarClickListener {
        void onCarClick(CarEntity car);
    }

    public interface OnFavClickListener {
        void onFavClick(int carId);
    }

    public CarAdapter(OnCarClickListener listener,
                      OnFavClickListener favListener) {

        this.listener = listener;
        this.favListener = favListener;
    }

    public void setCars(List<CarWithFav> cars) {
        this.cars = cars;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemCarBinding binding = ItemCarBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.bind(cars.get(position));
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {

        ItemCarBinding binding;

        public CarViewHolder(ItemCarBinding b) {
            super(b.getRoot());
            binding = b;
        }

        void bind(CarWithFav item) {

            CarEntity car = item.car;

            binding.tvName.setText(car.name);
            binding.tvCategory.setText(car.category);

            String priceText = "$" + car.dailyPrice + " " + binding.getRoot().getContext().getString(R.string.per_day);
            binding.tvPrice.setText(priceText);

            binding.imgCar.setImageResource(car.imageRes);

            if (car.available) {
                binding.tvAvailability.setText(binding.getRoot().getContext().getString(R.string.available));
                binding.tvAvailability.setBackgroundResource(R.drawable.bg_badge_available);
            } else {
                binding.tvAvailability.setText(binding.getRoot().getContext().getString(R.string.not_available));
                binding.tvAvailability.setBackgroundResource(R.drawable.bg_badge_not_available);
            }

            if (item.isFav) {
                binding.btnFav.setImageResource(R.drawable.ic_heart_fill);
            } else {
                binding.btnFav.setImageResource(R.drawable.ic_heart_outline);
            }

            binding.getRoot().setOnClickListener(v ->
                    listener.onCarClick(car)
            );

            binding.btnFav.setOnClickListener(v ->
                    favListener.onFavClick(car.id)
            );
        }
    }
}