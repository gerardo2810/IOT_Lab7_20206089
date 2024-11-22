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

public class UserHomeActivity extends AppCompatActivity {
    private RecyclerView rvBusLines;
    private TextView tvBalance;
    private double balance = 50.0; // Inicializar saldo por defecto
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rvBusLines = findViewById(R.id.rv_bus_lines);
        tvBalance = findViewById(R.id.tv_balance);
        userNameText = findViewById(R.id.toolbar_user_name); // Referencia al TextView dentro del Toolbar
        findViewById(R.id.logout_button).setOnClickListener(v -> logout());

        // Obtener nombre del usuario logueado
        fetchUserName();

        // Configurar RecyclerView
        rvBusLines.setLayoutManager(new LinearLayoutManager(this));
        fetchBusesFromDatabase();

        // Mostrar saldo
        tvBalance.setText("Saldo restante: S/. " + balance);
    }

    private void fetchUserName() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Verificar si los campos existen
                String nombre = documentSnapshot.getString("nombre");
                String apellido = documentSnapshot.getString("apellido");

                // Imprimir en consola para depuración
                System.out.println("Nombre: " + nombre);
                System.out.println("Apellido: " + apellido);

                if (nombre != null && apellido != null) {
                    // Actualizar el texto en el Toolbar
                    userNameText.setText("Bienvenido, " + nombre + " " + apellido);
                } else {
                    userNameText.setText("Bienvenido, Usuario");
                    Toast.makeText(this, "Faltan datos de nombre y apellido del usuario.", Toast.LENGTH_SHORT).show();
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

    private void fetchBusesFromDatabase() {
        db.collection("buses").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Bus> buses = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                try {
                    // Mapear datos de Firestore a objeto Bus
                    String id = document.getId();
                    String mainImageUrl = document.getString("mainImageUrl");
                    List<String> imageUrls = (List<String>) document.get("imageUrls");
                    double ticketPrice = document.getDouble("ticketPrice");
                    double subscriptionPrice = document.getDouble("subscriptionPrice");

                    // Crear objeto Bus
                    if (id != null && mainImageUrl != null && imageUrls != null) {
                        buses.add(new Bus(id, mainImageUrl, imageUrls, ticketPrice, subscriptionPrice));
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error al procesar datos del bus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            // Configurar RecyclerView con datos de Firestore
            BusAdapter adapter = new BusAdapter(buses, this::onBusDetailsClicked);
            rvBusLines.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al obtener datos de los buses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void logout() {
        auth.signOut();
        Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void onBusDetailsClicked(Bus bus) {
        Intent intent = new Intent(this, BusDetailsActivity.class);
        intent.putExtra("bus", bus);
        startActivity(intent);
    }
}
