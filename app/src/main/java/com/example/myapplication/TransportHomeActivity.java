package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TransportHomeActivity extends AppCompatActivity {
    private RecyclerView rvBusLines;
    private TextView tvRevenue;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_home);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rvBusLines = findViewById(R.id.rv_transport_bus_lines);
        tvRevenue = findViewById(R.id.tv_transport_revenue);
        userNameText = findViewById(R.id.toolbar_user_name);

        ImageButton logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> logout());

        rvBusLines.setLayoutManager(new LinearLayoutManager(this));
        fetchUserName();
        fetchBusLines();
        calculateRevenue();
    }

    private void fetchUserName() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String nombre = documentSnapshot.getString("nombre");
                String apellido = documentSnapshot.getString("apellido");

                System.out.println("Nombre: " + nombre);
                System.out.println("Apellido: " + apellido);

                if (nombre != null && apellido != null) {
                    userNameText.setText("Bienvenido, " + nombre + " " + apellido);
                } else {
                    userNameText.setText("Bienvenido, Usuario");
                    Toast.makeText(this, "Faltan datos del usuario.", Toast.LENGTH_SHORT).show();
                }
            } else {
                userNameText.setText("Bienvenido, Usuario");
                Toast.makeText(this, "No se encontraron datos del usuario.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            userNameText.setText("Bienvenido, Usuario");
            Toast.makeText(this, "Error al obtener datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchBusLines() {
        db.collection("buses").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Bus> buses = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String imageUrl = document.getString("mainImageUrl");
                Log.d("FirestoreCheck", "Bus ID: " + document.getId() + " Image URL: " + imageUrl);

                if (imageUrl == null || imageUrl.isEmpty()) {
                    Log.e("FirestoreError", "Image URL is null or empty for Bus ID: " + document.getId());
                }

                try {
                    String id = document.getId();
                    double ticketPrice = document.getDouble("ticketPrice");
                    double subscriptionPrice = document.getDouble("subscriptionPrice");
                    boolean hasSubscription = document.getBoolean("hasSubscription") != null
                            ? document.getBoolean("hasSubscription")
                            : false;
                    List<String> gallery = (List<String>) document.get("imageUrls");

                    // Crear objeto Bus
                    buses.add(new Bus(id, imageUrl, gallery, ticketPrice, subscriptionPrice, hasSubscription));
                } catch (Exception e) {
                    Toast.makeText(this, "Error al procesar datos del bus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            // Configurar RecyclerView con datos de Firestore
            TransportBusAdapter adapter = new TransportBusAdapter(buses, this::onEditBusClicked);
            rvBusLines.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al obtener las líneas de buses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void calculateRevenue() {
        db.collection("revenue").document("monthly").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Double revenue = documentSnapshot.getDouble("total");
                if (revenue != null) {
                    tvRevenue.setText("Monto recaudado: S/. " + revenue);
                } else {
                    tvRevenue.setText("Monto recaudado: S/. 0.0");
                }
            } else {
                tvRevenue.setText("Monto recaudado: S/. 0.0");
            }
        }).addOnFailureListener(e -> {
            tvRevenue.setText("Monto recaudado: S/. 0.0");
            Toast.makeText(this, "Error al obtener el monto recaudado: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void onEditBusClicked(Bus bus) {
        Intent intent = new Intent(this, BusEditActivity.class);
        intent.putExtra("bus", bus);
        startActivity(intent);
    }

    private void logout() {
        auth.signOut();
        Intent intent = new Intent(TransportHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
