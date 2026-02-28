package com.example.carrentalapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    public interface OnCategoryClick {
        void onClick(String category); // category key (EN) for filtering
    }

    private final List<String> categories;
    private final OnCategoryClick listener;

    private int selectedPos = 0;

    public CategoryAdapter(List<String> categories, OnCategoryClick listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        String catKey = categories.get(position);

        // ✅ عرض مترجم
        h.tv.setText(getLocalizedCategory(h.itemView.getContext(), catKey));

        // ✅ highlight selected
        boolean selected = position == selectedPos;
        h.card.setStrokeWidth(selected ? 2 : 1);
        h.card.setStrokeColor(selected ? 0xFF6A1B9A : 0xFFE0E0E0); // بنفسجي / رمادي
        h.tv.setTextColor(selected ? 0xFF6A1B9A : 0xFF1A1A1A);

        h.itemView.setOnClickListener(v -> {
            int old = selectedPos;
            selectedPos = h.getAdapterPosition();

            notifyItemChanged(old);
            notifyItemChanged(selectedPos);

            // ✅ رجّع القيمة الأصلية (EN) للفلترة
            listener.onClick(catKey);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private String getLocalizedCategory(Context ctx, String key) {
        int resId = R.string.cat_all;

        if ("All".equalsIgnoreCase(key)) resId = R.string.cat_all;
        else if ("Family".equalsIgnoreCase(key)) resId = R.string.cat_family;
        else if ("SUV".equalsIgnoreCase(key)) resId = R.string.cat_suv;
        else if ("Economy".equalsIgnoreCase(key)) resId = R.string.cat_economy;
        else if ("Sport".equalsIgnoreCase(key)) resId = R.string.cat_sport;
        else if ("Electric".equalsIgnoreCase(key)) resId = R.string.cat_electric;
        else if ("Luxury".equalsIgnoreCase(key)) resId = R.string.cat_luxury;

        return ctx.getString(resId);
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tv;
        MaterialCardView card;

        Holder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tvCategory);
            card = itemView.findViewById(R.id.cardCategory);
        }
    }
}