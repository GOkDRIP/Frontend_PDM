package com.fitlife.ui;

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

public class EditProfileActivity extends AppCompatActivity {
    private EditText etEdad, etPeso, etAltura, etObjetivo;
    private Spinner spinnerNivel;
    private Button btnSave;
    private FitLifeService api;
    private ArrayAdapter<NivelActividad> nivelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inicializar vistas
        etEdad       = findViewById(R.id.etEdad);
        etPeso       = findViewById(R.id.etPeso);
        etAltura     = findViewById(R.id.etAltura);
        spinnerNivel = findViewById(R.id.spNivel);
        etObjetivo   = findViewById(R.id.etObjetivo);
        btnSave      = findViewById(R.id.btnSave);

        // Configurar Spinner de nivel de actividad usando tu enum
        nivelAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                NivelActividad.values()
        );
        nivelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(nivelAdapter);
        spinnerNivel.setPrompt(getString(R.string.prompt_nivel_actividad));

        // Inicializar API
        api = RetrofitClient.getService();

        // Cargar datos actuales para editar
        api.getProfile().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    ProfileResponse u = response.body();
                    etEdad.setText(String.valueOf(u.edad));
                    etPeso.setText(String.valueOf(u.peso));
                    etAltura.setText(String.valueOf(u.altura));
                    etObjetivo.setText(u.objetivo != null ? u.objetivo : "");
                    if (u.nivelActividad != null) {
                        try {
                            NivelActividad nivelEnum = NivelActividad.valueOf(u.nivelActividad);
                            int pos = nivelAdapter.getPosition(nivelEnum);
                            spinnerNivel.setSelection(pos);
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Error cargando perfil", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Fallo de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Guardar cambios al pulsar botón
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        EditProfileRequest req = new EditProfileRequest();
        try {
            req.edad   = Integer.parseInt(etEdad.getText().toString().trim());
            req.peso   = Double.parseDouble(etPeso.getText().toString().trim());
            req.altura = Double.parseDouble(etAltura.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Edad, peso o altura inválidos", Toast.LENGTH_SHORT).show();
            return;
        }
        NivelActividad sel = (NivelActividad) spinnerNivel.getSelectedItem();
        req.nivelActividad = sel.toApiValue();
        req.objetivo       = etObjetivo.getText().toString().trim();

        api.editProfile(req).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    Toast.makeText(EditProfileActivity.this,
                            "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    // Informamos de éxito al ProfileActivity
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String msg = (response.body() != null)
                            ? response.body().message
                            : "Error al guardar";
                    Toast.makeText(EditProfileActivity.this,
                            msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Fallo de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
