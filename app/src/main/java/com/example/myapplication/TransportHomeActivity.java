package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_home);

        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rvBusLines = findViewById(R.id.rv_transport_bus_lines);
        tvRevenue = findViewById(R.id.tv_transport_revenue);

        // Configurar RecyclerView
        rvBusLines.setLayoutManager(new LinearLayoutManager(this));
        fetchBusLines();
        calculateRevenue();
    }

    private void fetchBusLines() {
        db.collection("buses").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Bus> buses = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String id = document.getId();
                String imageUrl = document.getString("mainImageUrl");
                double ticketPrice = document.getDouble("ticketPrice");
                double subscriptionPrice = document.getDouble("subscriptionPrice");
                List<String> gallery = (List<String>) document.get("galleryImages");

                buses.add(new Bus(id, imageUrl, gallery, ticketPrice, subscriptionPrice));
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
                double revenue = documentSnapshot.getDouble("total");
                tvRevenue.setText("Monto recaudado: S/. " + revenue);
            } else {
                tvRevenue.setText("Monto recaudado: S/. 0.0");
            }
        });
    }

    private void onEditBusClicked(Bus bus) {
        Intent intent = new Intent(this, BusEditActivity.class);
        intent.putExtra("bus", bus);
        startActivity(intent);
    }
}
