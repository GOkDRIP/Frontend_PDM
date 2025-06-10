package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.ProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_EDIT_PROFILE = 1001;

    private TextView tvNombre, tvEmail, tvEdad, tvPeso, tvAltura, tvNivel, tvObjetivo;
    private Button btnEditProfile;
    private FitLifeService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar vistas
        tvNombre      = findViewById(R.id.tvNombre);
        tvEmail       = findViewById(R.id.tvEmail);
        tvEdad        = findViewById(R.id.tvEdad);
        tvPeso        = findViewById(R.id.tvPeso);
        tvAltura      = findViewById(R.id.tvAltura);
        tvNivel       = findViewById(R.id.tvNivel);
        tvObjetivo    = findViewById(R.id.tvObjetivo);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Configurar Retrofit
        api = RetrofitClient.getService();

        // Evento para editar perfil con resultado
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, REQUEST_EDIT_PROFILE);
        });

        // Cargar datos del perfil inicialmente
        loadProfile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            // Refrescamos datos después de editar
            loadProfile();
        }
    }

    private void loadProfile() {
        api.getProfile().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse perfil = response.body();
                    if (perfil.success) {
                        tvNombre.setText(perfil.nombre);
                        tvEmail.setText(perfil.email);
                        tvEdad.setText(perfil.edad + " años");
                        tvPeso.setText(perfil.peso + " kg");
                        tvAltura.setText(perfil.altura + " cm");
                        tvNivel.setText(perfil.nivelActividad != null ? perfil.nivelActividad : "");
                        tvObjetivo.setText(perfil.objetivo != null ? perfil.objetivo : "");
                    } else {
                        Toast.makeText(ProfileActivity.this,
                                perfil.message != null ? perfil.message : "Error al cargar perfil",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this,
                            "Error en la respuesta del servidor",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,
                        "Fallo de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
