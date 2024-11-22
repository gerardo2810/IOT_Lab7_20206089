package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BusEditActivity extends AppCompatActivity {
    private RecyclerView rvGalleryImages;
    private EditText etNewImageUrl;
    private Button btnAddImage, btnSaveChanges;
    private FirebaseFirestore db;
    private Bus bus;
    private GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_edit);

        rvGalleryImages = findViewById(R.id.rv_gallery_images);
        etNewImageUrl = findViewById(R.id.et_new_image_url);
        btnAddImage = findViewById(R.id.btn_add_image);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        db = FirebaseFirestore.getInstance();
        bus = (Bus) getIntent().getSerializableExtra("bus");

        // Inicializar la lista de imágenes
        List<String> galleryImages = new ArrayList<>();
        if (bus.getImageUrls() != null) {
            galleryImages.addAll(bus.getImageUrls());
        }

        // Configurar el adaptador de la galería
        adapter = new GalleryAdapter(galleryImages, position -> {
            // Eliminar imagen seleccionada
            galleryImages.remove(position);
            adapter.notifyItemRemoved(position);
        });

        rvGalleryImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGalleryImages.setAdapter(adapter);

        // Agregar nueva imagen
        btnAddImage.setOnClickListener(v -> {
            String newImageUrl = etNewImageUrl.getText().toString().trim();
            if (!newImageUrl.isEmpty()) {
                galleryImages.add(newImageUrl);
                adapter.notifyItemInserted(galleryImages.size() - 1);
                etNewImageUrl.setText("");
            } else {
                Toast.makeText(this, "Por favor, ingrese una URL válida.", Toast.LENGTH_SHORT).show();
            }
        });

        // Guardar cambios en Firestore
        btnSaveChanges.setOnClickListener(v -> {
            db.collection("buses").document(bus.getId())
                    .update("imageUrls", galleryImages)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Cambios guardados correctamente.", Toast.LENGTH_SHORT).show();
                        bus.setImageUrls(galleryImages);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar cambios: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
