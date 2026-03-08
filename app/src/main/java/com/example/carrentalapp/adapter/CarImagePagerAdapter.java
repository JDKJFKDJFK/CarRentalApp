package com.example.carrentalapp.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarImagePagerAdapter extends RecyclerView.Adapter<CarImagePagerAdapter.Holder> {

    private final int[] images;

    public CarImagePagerAdapter(int[] images) {
        this.images = (images == null) ? new int[0] : images;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView iv = new ImageView(parent.getContext());
        iv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        iv.setAdjustViewBounds(false);
        iv.setDrawingCacheEnabled(true);
        iv.setLayerType(ImageView.LAYER_TYPE_HARDWARE, null);

        return new Holder(iv);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imageView.setImageResource(images[position]);

        Drawable d = holder.imageView.getDrawable();
        if (d != null) {
            d.setFilterBitmap(true);
            d.setDither(true);
        }
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Holder(@NonNull ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }
}