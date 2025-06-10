package com.fitlife.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.model.LoginRequest;
import com.fitlife.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class NutricionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        getLayoutInflater()
                .inflate(R.layout.fragment_nutricion,
                        findViewById(R.id.container), true);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.navigation_nutricion);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_entrenamiento) {
                startActivity(new Intent(this, EntrenamientoActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            return false;
        });
    }
}
