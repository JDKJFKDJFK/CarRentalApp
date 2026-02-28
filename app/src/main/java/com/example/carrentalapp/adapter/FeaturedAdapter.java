package com.example.carrentalapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.model.CarWithFav;

import java.util.ArrayList;
import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.Holder> {

    public interface Listener {
        void onClick(CarWithFav item);
    }

    private final List<CarWithFav> list = new ArrayList<>();
    private final Listener listener;

    public FeaturedAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setList(List<CarWithFav> newList) {
        list.clear();
        if (newList != null) list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_car, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        CarWithFav item = list.get(position);

        h.img.setImageResource(item.car.imageRes);
        h.name.setText(item.car.name);


        String priceText = "$" + item.car.dailyPrice + " " + h.itemView.getContext().getString(R.string.per_day);
        h.price.setText(priceText);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price;

        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCar);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);
        }
    }
}