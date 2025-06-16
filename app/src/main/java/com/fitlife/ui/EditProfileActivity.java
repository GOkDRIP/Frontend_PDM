package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.EditProfileRequest;
import com.fitlife.model.GenericResponse;
import com.fitlife.model.NivelActividad;
import com.fitlife.model.ProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.fitlife.utils.SessionManager;
import com.google.android.material.button.MaterialButton;



public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_EDIT_PROFILE = 1001;
    private EditText etEdad, etPeso, etAltura, etObjetivo;
    private Spinner spinnerNivel;
    private MaterialButton btnSave, btnLogout;
    private FitLifeService api;
    private ArrayAdapter<NivelActividad> nivelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // ---------- ENLAZAR VISTAS ----------
        etEdad        = findViewById(R.id.etEdad);
        etPeso        = findViewById(R.id.etPeso);
        etAltura      = findViewById(R.id.etAltura);
        spinnerNivel  = findViewById(R.id.spNivel);
        etObjetivo    = findViewById(R.id.etObjetivo);

        btnSave   = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);   // botón “Cerrar sesión”

        // ---------- SPINNER NIVEL DE ACTIVIDAD ----------
        nivelAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                NivelActividad.values()
        );
        nivelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(nivelAdapter);

        // ---------- RETROFIT ----------
        api = RetrofitClient.getService();

        // ---------- CARGAR PERFIL ----------
        loadProfile();

        // ---------- LISTENERS ----------
        btnSave.setOnClickListener(v -> saveProfile());

        btnLogout.setOnClickListener(v -> {
            // 1) Borra credenciales guardadas
            new SessionManager(getApplicationContext()).logout();

            // 2) Lanza LoginActivity limpiando el back-stack
            Intent i = new Intent(EditProfileActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            // No hace falta finishAffinity() porque CLEAR_TASK ya vacía la pila
        });
    }


    private void loadProfile() {
        api.getProfile().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null && resp.body().success) {
                    ProfileResponse u = resp.body();
                    etEdad.setText(String.valueOf(u.edad));
                    etPeso.setText(String.valueOf(u.peso));
                    etAltura.setText(String.valueOf(u.altura));
                    etObjetivo.setText(u.objetivo != null ? u.objetivo : "");
                    if (u.nivelActividad != null) {
                        try {
                            NivelActividad na = NivelActividad.valueOf(u.nivelActividad);
                            int pos = nivelAdapter.getPosition(na);
                            spinnerNivel.setSelection(pos);
                        } catch (IllegalArgumentException ignored) {}
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Error al cargar perfil", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveProfile() {
        EditProfileRequest req = new EditProfileRequest();
        try {
            req.edad   = Integer.parseInt(etEdad.getText().toString().trim());
            req.peso   = Double.parseDouble(etPeso.getText().toString().trim());
            req.altura = Double.parseDouble(etAltura.getText().toString().trim());
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Edad, peso o altura inválidos", Toast.LENGTH_SHORT).show();
            return;
        }
        NivelActividad sel = (NivelActividad) spinnerNivel.getSelectedItem();
        req.nivelActividad = sel.toApiValue();
        req.objetivo       = etObjetivo.getText().toString().trim();

        api.editProfile(req).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    GenericResponse gr = resp.body();
                    // Mostramos siempre el mensaje tal como viene
                    String msg = gr.message != null && !gr.message.isEmpty()
                            ? gr.message
                            : "Perfil Actualizado";
                    Toast.makeText(EditProfileActivity.this,
                            msg, Toast.LENGTH_LONG).show();

                    // Solo cerramos si fue un éxito
                        setResult(RESULT_OK);
                        finish();
                } else {
                    // En caso de error HTTP
                    Toast.makeText(EditProfileActivity.this,
                            "Error al guardar perfil: código " + resp.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Fallo de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}