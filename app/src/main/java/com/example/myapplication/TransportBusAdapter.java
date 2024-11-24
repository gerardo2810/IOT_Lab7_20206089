package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TransportBusAdapter extends RecyclerView.Adapter<TransportBusAdapter.BusViewHolder> {

    private List<Bus> busList;
    private OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEditClick(Bus bus);
    }

    public TransportBusAdapter(List<Bus> busList, OnEditClickListener listener) {
        this.busList = busList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_transport, parent, false);
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
        private Button btnEdit;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBusId = itemView.findViewById(R.id.tv_bus_id);
            ivBusImage = itemView.findViewById(R.id.iv_bus_image);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }

        public void bind(Bus bus, OnEditClickListener listener) {
            tvBusId.setText(bus.getId());
            Glide.with(itemView.getContext()).load(bus.getMainImageUrl()).into(ivBusImage);

            btnEdit.setOnClickListener(v -> listener.onEditClick(bus));
        }

    }
}
