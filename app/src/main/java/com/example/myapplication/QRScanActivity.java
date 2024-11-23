package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QRScanActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private double balance;
    private String userId;

    private long entryTime; // Para calcular la duración del viaje

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Obtener saldo inicial del usuario
        fetchUserBalance();

        // Simular escaneo del QR
        simulateQRScan("busID_123", true); // Escanear al ingresar
        simulateQRScan("busID_123", false); // Escanear al retirarse
    }

    private void fetchUserBalance() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double balanceValue = documentSnapshot.getDouble("balance");
                        balance = balanceValue != null ? balanceValue : 50.0;
                    } else {
                        balance = 50.0;
                        initializeBalance();
                    }
                })
                .addOnFailureListener(e -> {
                    balance = 50.0;
                    Toast.makeText(this, "Error al obtener el saldo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void initializeBalance() {
        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("balance", 50.0);
        db.collection("users").document(userId).set(userUpdate);
    }

    private void simulateQRScan(String busId, boolean isEntry) {
        if (isEntry) {
            processEntry(busId);
        } else {
            processExit(busId);
        }
    }

    private void processEntry(String busId) {
        db.collection("buses").document(busId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                double ticketPrice = documentSnapshot.getDouble("ticketPrice");
                boolean hasSubscription = documentSnapshot.getBoolean("hasSubscription");

                if (!hasSubscription) {
                    if (balance >= ticketPrice) {
                        balance -= ticketPrice;
                        updateBalance();
                        entryTime = System.currentTimeMillis();
                        Toast.makeText(this, "Entrada registrada. Saldo actual: S/. " + balance, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Saldo insuficiente.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Entrada registrada. Usuario con suscripción activa.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Bus no encontrado.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processExit(String busId) {
        long exitTime = System.currentTimeMillis();
        long travelDuration = (exitTime - entryTime) / (1000 * 60); // Convertir a minutos

        double cashbackPercentage = travelDuration < 15 ? 0.20 : 0.05;
        double cashback = cashbackPercentage * balance;

        balance += cashback;
        updateBalance();

        Toast.makeText(this, "Salida registrada. Cashback recibido: S/. " + cashback +
                ". Saldo actual: S/. " + balance, Toast.LENGTH_SHORT).show();
    }

    private void updateBalance() {
        db.collection("users").document(userId)
                .update("balance", balance)
                .addOnSuccessListener(aVoid -> {
                    // Saldo actualizado en Firestore
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al actualizar el saldo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
