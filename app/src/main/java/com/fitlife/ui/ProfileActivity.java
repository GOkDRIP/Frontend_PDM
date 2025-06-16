package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.GenericResponse;
import com.fitlife.model.ProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_EDIT_PROFILE = 1001;

    private TextView tvNombre, tvEmail, tvEdad, tvPeso, tvAltura, tvNivel, tvObjetivo;
    private Button btnEditProfile, btnEliminar;
    private FitLifeService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar vistas
        tvNombre       = findViewById(R.id.tvNombre);
        tvEmail        = findViewById(R.id.tvEmail);
        tvEdad         = findViewById(R.id.tvEdad);
        tvPeso         = findViewById(R.id.tvPeso);
        tvAltura       = findViewById(R.id.tvAltura);
        tvNivel        = findViewById(R.id.tvNivel);
        tvObjetivo     = findViewById(R.id.tvObjetivo);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEliminar    = findViewById(R.id.btnEliminarCuenta);

        // Configurar Retrofit
        api = RetrofitClient.getService();

        // Editar perfil
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, REQUEST_EDIT_PROFILE);
        });

        // Eliminar cuenta
        btnEliminar.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar cuenta")
                        .setMessage("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
                        .setPositiveButton("Eliminar", (dialog, which) -> doEliminarUsuario())
                        .setNegativeButton("Cancelar", null)
                        .show()
        );

        // Cargar datos
        loadProfile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            loadProfile();
        }
    }

    private void loadProfile() {
        api.getProfile().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    ProfileResponse perfil = resp.body();
                    if (perfil.success) {
                        tvNombre.setText(perfil.nombre);
                        tvEmail.setText(perfil.email);
                        tvEdad.setText(perfil.edad + " años");
                        tvPeso.setText(perfil.peso + " kg");
                        tvAltura.setText(perfil.altura + " cm");
                        tvNivel.setText(perfil.nivelActividad != null ? perfil.nivelActividad : "");
                        tvObjetivo.setText(perfil.objetivo       != null ? perfil.objetivo       : "");
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

    private void doEliminarUsuario() {
        api.eliminarUsuario().enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> resp) {
                if (resp.isSuccessful()) {
                    // HTTP 200 → borrado OK
                    Toast.makeText(
                            ProfileActivity.this,
                            "Usuario eliminado correctamente",
                            Toast.LENGTH_LONG
                    ).show();

                    // Redirigir a login
                    Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    // Cualquier otro código → error
                    Toast.makeText(
                            ProfileActivity.this,
                            "Error al eliminar cuenta: código " + resp.code(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(
                        ProfileActivity.this,
                        "Fallo de red: " + (t.getMessage() == null ? "desconocido" : t.getMessage()),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }



}
