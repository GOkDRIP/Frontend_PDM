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
    private static final int REQ_RUTINA     = 2001;
    private static final int REQ_OBJETIVOS  = 2002;   // nuevo request-code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        View fragmentView = getLayoutInflater()
                .inflate(R.layout.fragment_entrenamiento,
                        findViewById(R.id.container), true);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        // Registrar progreso
        fragmentView.<Button>findViewById(R.id.btn_registrar_progreso)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, RegistrarProgresoActivity.class))
                );

        // Listar progresos
        fragmentView.<Button>findViewById(R.id.btn_listar_progresos)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, ListarProgresosActivity.class))
                );

        // Ver rutina actual
        fragmentView.<Button>findViewById(R.id.btn_ir_rutina_principal)
                .setOnClickListener(v ->
                        startActivityForResult(
                                new Intent(this, VerRutinaActualActivity.class),
                                REQ_RUTINA
                        )
                );

        // NUEVO: Objetivos del día
        fragmentView.<Button>findViewById(R.id.btn_objetivos_dia)
                .setOnClickListener(v ->
                        startActivityForResult(
                                new Intent(this, ObjetivosDiaActivity.class),
                                REQ_OBJETIVOS
                        )
                );

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
        invalidateOptionsMenu();
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
