package com.example.carrentalapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.activity.CarDetailsActivity;
import com.example.carrentalapp.data.model.FavoriteWithCar;
import com.example.carrentalapp.databinding.ItemFavoriteBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    private List<FavoriteWithCar> list = new ArrayList<>();

    public interface OnRemoveClick {
        void onRemove(int carId);
    }

    public interface OnCarClick {
        void onClick(int carId);
    }

    private final OnRemoveClick removeListener;
    private final OnCarClick carClickListener;

    public FavoriteAdapter(OnRemoveClick removeListener, OnCarClick carClickListener) {
        this.removeListener = removeListener;
        this.carClickListener = carClickListener;
    }

    public void setList(List<FavoriteWithCar> list) {
        this.list = (list == null) ? new ArrayList<>() : list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBinding binding =
                ItemFavoriteBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {

        FavoriteWithCar item = list.get(pos);

        h.binding.tvName.setText(item.car.name);
        h.binding.tvPrice.setText("$" + item.car.dailyPrice);
        h.binding.imgCar.setImageResource(item.car.imageRes);

        h.binding.getRoot().setOnClickListener(v -> {
            if (carClickListener != null) {
                carClickListener.onClick(item.car.id);
            } else {
                // fallback
                Intent i = new Intent(v.getContext(), CarDetailsActivity.class);
                i.putExtra("car_id", item.car.id);
                v.getContext().startActivity(i);
            }
        });

        h.binding.btnRemove.setOnClickListener(v -> {
            if (removeListener != null) removeListener.onRemove(item.car.id);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ItemFavoriteBinding binding;

        public Holder(ItemFavoriteBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}