package com.example.carrentalapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.Holder> {

    private final int[] layouts = new int[] {
            R.layout.item_onboarding_1,
            R.layout.item_onboarding_2,
            R.layout.item_onboarding_3
    };

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return layouts.length;
    }

    @Override
    public int getItemViewType(int position) {
        return layouts[position];
    }

    static class Holder extends RecyclerView.ViewHolder {
        Holder(@NonNull View itemView) { super(itemView); }
    }
}