package com.example.carrentalapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.R;
import com.example.carrentalapp.data.model.BookingWithCar;
import com.example.carrentalapp.databinding.ItemBookingBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.Holder> {

    private List<BookingWithCar> list = new ArrayList<>();
    private final OnDeleteClick listener;

    public interface OnDeleteClick {
        void onDelete(int bookingId);
    }

    public BookingAdapter(OnDeleteClick listener) {
        this.listener = listener;
    }

    public void setList(List<BookingWithCar> list) {
        this.list = (list == null) ? new ArrayList<>() : list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingBinding binding = ItemBookingBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {

        BookingWithCar item = list.get(pos);

        h.binding.tvCar.setText(item.car.name);
        h.binding.imgCar.setImageResource(item.car.imageRes);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String from = sdf.format(new Date(item.booking.pickupDateTime));
        String to = sdf.format(new Date(item.booking.returnDateTime));
        h.binding.tvDate.setText(from + " - " + to);

        h.binding.tvPrice.setText("$" + String.format(Locale.getDefault(), "%.2f", item.booking.totalPrice));

        boolean isActive = "ACTIVE".equalsIgnoreCase(item.booking.status);

        if (isActive) {
            h.binding.tvStatus.setText(h.itemView.getContext().getString(R.string.status_active));
            h.binding.tvStatus.setBackgroundResource(R.drawable.bg_badge_available);
            h.binding.btnDelete.setVisibility(View.VISIBLE);
            h.binding.btnDelete.setOnClickListener(v -> listener.onDelete(item.booking.id));
        } else {
            h.binding.tvStatus.setText(h.itemView.getContext().getString(R.string.status_ended));
            h.binding.tvStatus.setBackgroundResource(R.drawable.bg_badge_not_available);
            h.binding.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ItemBookingBinding binding;

        public Holder(ItemBookingBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}