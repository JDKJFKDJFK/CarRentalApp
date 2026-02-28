package com.example.carrentalapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.entity.CarEntity;
import com.example.carrentalapp.data.model.CarWithFav;

import java.util.ArrayList;
import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.Holder> {

    private List<CarWithFav> list = new ArrayList<>();

    private OnCarClick carClick;
    private OnFavClick favClick;


    public interface OnCarClick {
        void onClick(CarEntity car);
    }

    public interface OnFavClick {
        void onFav(int carId);
    }


    public CarsAdapter(OnCarClick carClick,
                       OnFavClick favClick) {

        this.carClick = carClick;
        this.favClick = favClick;
    }


    public void setList(List<CarWithFav> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);

        return new Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {

        CarWithFav item = list.get(pos);

        h.name.setText(item.car.name);
        h.cat.setText(item.car.category);
        h.price.setText("$" + item.car.dailyPrice + " /day");


        if (item.isFav) {
            h.fav.setImageResource(R.drawable.ic_heart_fill);
        } else {
            h.fav.setImageResource(R.drawable.ic_heart_outline);
        }



        h.itemView.setOnClickListener(v ->
                carClick.onClick(item.car)
        );


        h.fav.setOnClickListener(v ->
                favClick.onFav(item.car.id)
        );
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    static class Holder extends RecyclerView.ViewHolder {

        ImageView img, fav;
        TextView name, cat, price;


        public Holder(@NonNull View v) {
            super(v);

            img = v.findViewById(R.id.imgCar);
            fav = v.findViewById(R.id.btnFav);

            name = v.findViewById(R.id.tvName);
            cat = v.findViewById(R.id.tvCategory);
            price = v.findViewById(R.id.tvPrice);
        }
    }
}
