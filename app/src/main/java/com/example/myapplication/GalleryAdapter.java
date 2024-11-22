package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<String> galleryImages;
    private OnImageDeleteListener listener;

    public interface OnImageDeleteListener {
        void onImageDelete(int position);
    }

    public GalleryAdapter(List<String> galleryImages, OnImageDeleteListener listener) {
        this.galleryImages = galleryImages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        String imageUrl = galleryImages.get(position);
        holder.bind(imageUrl, position, listener);
    }

    @Override
    public int getItemCount() {
        return galleryImages.size();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivGalleryImage;
        private Button btnDelete;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGalleryImage = itemView.findViewById(R.id.iv_gallery_image);
            btnDelete = itemView.findViewById(R.id.btn_delete_image);
        }

        public void bind(String imageUrl, int position, OnImageDeleteListener listener) {
            Glide.with(itemView.getContext()).load(imageUrl).into(ivGalleryImage);

            btnDelete.setOnClickListener(v -> listener.onImageDelete(position));
        }
    }
}
