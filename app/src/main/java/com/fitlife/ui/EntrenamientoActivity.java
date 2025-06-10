package com.fitlife.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EntrenamientoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Inflar fragmento dentro del contenedor
        View fragmentView = getLayoutInflater()
                .inflate(R.layout.fragment_entrenamiento,
                        findViewById(R.id.container), true);

        // Obtener botón desde la vista inflada
        Button btnRegistrar = fragmentView.findViewById(R.id.btn_registrar_progreso);
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(EntrenamientoActivity.this, RegistrarProgresoActivity.class);
            startActivity(intent);
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.navigation_entrenamiento);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_nutricion) {
                startActivity(new Intent(this, NutricionActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
