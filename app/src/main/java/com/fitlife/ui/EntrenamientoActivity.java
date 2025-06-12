package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EntrenamientoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View fragmentView = getLayoutInflater()
                .inflate(R.layout.fragment_entrenamiento,
                        findViewById(R.id.container), true);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        // botón para registrar progreso (ya lo tenías)
        Button btnRegistrar = fragmentView.findViewById(R.id.btn_registrar_progreso);
        btnRegistrar.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrarProgresoActivity.class));
        });

        // ==== NUEVO botón para listar progresos ====
        Button btnListar = fragmentView.findViewById(R.id.btn_listar_progresos);
        btnListar.setOnClickListener(v -> {
            startActivity(new Intent(this, ListarProgresosActivity.class));
        });
        // ==========================================

        setupBottomNavigation();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu(); // Refrescar el menú al volver
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
