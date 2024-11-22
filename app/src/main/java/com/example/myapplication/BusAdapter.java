package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import android.view.View;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;


public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<Bus> busList;
    private OnBusClickListener listener;

    public interface OnBusClickListener {
        void onBusClick(Bus bus);
    }

    public BusAdapter(List<Bus> busList, OnBusClickListener listener) {
        this.busList = busList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.bind(bus, listener);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBusId;
        private ImageView ivBusImage;
        private Button btnDetails;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBusId = itemView.findViewById(R.id.tv_bus_id);
            ivBusImage = itemView.findViewById(R.id.iv_bus_image);
            btnDetails = itemView.findViewById(R.id.btn_details);
        }

        public void bind(Bus bus, OnBusClickListener listener) {
            tvBusId.setText(bus.getId());
            Glide.with(itemView.getContext()).load(bus.getImageUrl()).into(ivBusImage);

            btnDetails.setOnClickListener(v -> listener.onBusClick(bus));
        }
    }
}
