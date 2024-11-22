package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class BusDetailsActivity extends AppCompatActivity {
    private double balance = 50.0; // Saldo inicial del usuario
    private ViewPager vpBusImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        // Obtener datos del bus desde el Intent
        Bus bus = (Bus) getIntent().getSerializableExtra("bus");

        // Referencias a las vistas
        vpBusImages = findViewById(R.id.vp_bus_images);
        TextView tvTicketPrice = findViewById(R.id.tv_ticket_price);
        TextView tvSubscriptionPrice = findViewById(R.id.tv_subscription_price);
        Button btnSubscribe = findViewById(R.id.btn_subscribe);

        // Configurar precios
        tvTicketPrice.setText("Precio unitario: S/. " + bus.getTicketPrice());
        tvSubscriptionPrice.setText("Precio suscripción mensual: S/. " + bus.getSubscriptionPrice());

        // Configurar carrusel de imágenes
        List<String> imageUrls = bus.getImageUrls(); // Lista de imágenes desde el objeto `Bus`
        if (imageUrls != null && !imageUrls.isEmpty()) {
            BusImagePagerAdapter adapter = new BusImagePagerAdapter(this, imageUrls);
            vpBusImages.setAdapter(adapter);
        }

        // Configurar acción del botón de suscripción
        btnSubscribe.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar suscripción")
                    .setMessage("¿Deseas suscribirte al bus por S/. " + bus.getSubscriptionPrice() + "?")
                    .setPositiveButton("Sí", (dialog, which) -> subscribeToBus(bus))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void subscribeToBus(Bus bus) {
        if (balance >= bus.getSubscriptionPrice()) {
            balance -= bus.getSubscriptionPrice();
            Toast.makeText(this, "Suscripción realizada. Saldo restante: S/. " + balance, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saldo insuficiente para suscribirte a este bus.", Toast.LENGTH_SHORT).show();
        }
    }
}
