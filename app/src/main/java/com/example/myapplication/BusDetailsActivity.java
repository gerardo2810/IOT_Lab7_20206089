package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BusDetailsActivity extends AppCompatActivity {
    private double balance = 50.0; // Saldo inicial predeterminado
    private ViewPager vpBusImages;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        // Inicializar Firebase Auth y Firestore
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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

        // Obtener el saldo actual del usuario
        fetchUserBalance();

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

    private void fetchUserBalance() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Verificar si balance no es null
                        Double balanceValue = documentSnapshot.getDouble("balance");
                        if (balanceValue != null) {
                            balance = balanceValue;
                        } else {
                            // Si el saldo no existe en Firestore, inicializar con 50 soles
                            balance = 50.0;
                            initializeUserBalance(userId);
                        }
                    } else {
                        // Si el documento no existe, inicializar el saldo con 50 soles
                        balance = 50.0;
                        initializeUserBalance(userId);
                    }
                })
                .addOnFailureListener(e -> {
                    balance = 50.0; // Valor predeterminado en caso de error
                    Toast.makeText(this, "Error al obtener el saldo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void initializeUserBalance(String userId) {
        db.collection("users").document(userId)
                .update("balance", 50.0)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Saldo inicial configurado a S/. 50.0", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al inicializar el saldo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void subscribeToBus(Bus bus) {
        if (balance >= bus.getSubscriptionPrice()) {
            // Restar el costo de la suscripción del saldo actual
            balance -= bus.getSubscriptionPrice();

            // Actualizar el saldo en Firestore
            String userId = auth.getCurrentUser().getUid();
            db.collection("users").document(userId)
                    .update("balance", balance)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Suscripción realizada. Saldo restante: S/. " + balance, Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al actualizar el saldo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Saldo insuficiente para suscribirte a este bus.", Toast.LENGTH_SHORT).show();
        }
    }
}
