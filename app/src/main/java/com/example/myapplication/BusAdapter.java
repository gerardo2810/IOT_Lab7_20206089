package com.example.myapplication;

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

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<Bus> busList;
    private OnBusClickListener listener;

    // Interfaz para manejar clics en los elementos de la lista
    public interface OnBusClickListener {
        void onBusClick(Bus bus);
    }

    // Constructor del adaptador
    public BusAdapter(List<Bus> busList, OnBusClickListener listener) {
        this.busList = busList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada elemento de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        // Enlazar los datos del bus a la vista
        Bus bus = busList.get(position);
        holder.bind(bus, listener);
    }

    @Override
    public int getItemCount() {
        // Retornar la cantidad de elementos en la lista
        return busList.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBusId;
        private ImageView ivBusImage;
        private Button btnDetails;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas del layout
            tvBusId = itemView.findViewById(R.id.tv_bus_id);
            ivBusImage = itemView.findViewById(R.id.iv_bus_image);
            btnDetails = itemView.findViewById(R.id.btn_details);
        }

        public void bind(Bus bus, OnBusClickListener listener) {
            // Configurar el texto del ID del bus
            tvBusId.setText(bus.getId());

            // Cargar la imagen principal del bus usando Glide
            Glide.with(itemView.getContext())
                    .load(bus.getMainImageUrl()) // Método corregido para obtener la URL de la imagen principal
                    .into(ivBusImage);

            // Configurar el clic en el botón de detalles
            btnDetails.setOnClickListener(v -> listener.onBusClick(bus));
        }
    }
}
