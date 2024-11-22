package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BusDetailsActivity extends AppCompatActivity {
    private double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        // Obtener datos del bus y del usuario
        Bus bus = (Bus) getIntent().getSerializableExtra("bus");

        TextView tvTicketPrice = findViewById(R.id.tv_ticket_price);
        TextView tvSubscriptionPrice = findViewById(R.id.tv_subscription_price);
        Button btnSubscribe = findViewById(R.id.btn_subscribe);

        tvTicketPrice.setText("Precio unitario: S/. " + bus.getTicketPrice());
        tvSubscriptionPrice.setText("Precio suscripción mensual: S/. " + bus.getSubscriptionPrice());

        // Manejar suscripción
        btnSubscribe.setOnClickListener(v -> {
            if (balance >= bus.getSubscriptionPrice()) {
                balance -= bus.getSubscriptionPrice();
                Toast.makeText(this, "Suscripción realizada. Saldo: S/. " + balance, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
