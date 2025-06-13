// File: NutricionActivity.java
package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.MaterialToolbar;
import com.fitlife.R;

public class NutricionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // 1) Contenedor de fragmentos
        ViewGroup container = findViewById(R.id.container);

        // 2) Inflamos fragment_nutricion.xml SIN attachToRoot
        View fragmentView = getLayoutInflater().inflate(
                R.layout.fragment_nutricion,
                container,
                false
        );

        // 3) Lo añadimos manualmente al contenedor
        container.addView(fragmentView);

        // 4) Botón "Listar comidas"
        Button btnListar = fragmentView.findViewById(R.id.btnListarComidas);
        btnListar.setOnClickListener(v ->
                startActivity(new Intent(this, ListarComidasActivity.class))
        );

        // 4b) NUEVO: Botón "Ver Feed social"
        Button btnFeed = fragmentView.findViewById(R.id.btnVerFeed);
        btnFeed.setOnClickListener(v ->
                startActivity(new Intent(this, FeedActivity.class))
        );

        // 5) Configuramos la AppBar y menú
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        // 6) Navegación inferior
        setupBottomNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.navigation_nutricion);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_entrenamiento) {
                startActivity(new Intent(this, EntrenamientoActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
