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
import com.fitlife.model.ErrorResponse;
import com.fitlife.model.NivelActividad;
import com.fitlife.model.RegisterRequest;
import com.fitlife.model.RegisterResponse;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etNombre, etEmail, etPassword, etObjetivo, etEdad, etPeso, etAltura;
    private Spinner spinnerSexo, spinnerNivel;
    private Button btnRegister;
    private FitLifeService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Referencias a vistas
        etNombre   = findViewById(R.id.etNombre);
        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etObjetivo = findViewById(R.id.etObjetivo);
        etEdad     = findViewById(R.id.etEdad);
        etPeso     = findViewById(R.id.etPeso);
        etAltura   = findViewById(R.id.etAltura);
        spinnerSexo  = findViewById(R.id.spinnerSexo);
        spinnerNivel = findViewById(R.id.spinnerNivel);
        btnRegister  = findViewById(R.id.btnRegister);

        // Configurar Retrofit
        api = RetrofitClient.getService();

        // Adapter para sexo (F/M u opciones locales)
        ArrayAdapter<CharSequence> sexoAdapter = ArrayAdapter.createFromResource(
                this, R.array.sexo_array, android.R.layout.simple_spinner_item);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(sexoAdapter);

        // Adapter para nivel de actividad (enum)
        ArrayAdapter<NivelActividad> nivelAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, NivelActividad.values());
        nivelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(nivelAdapter);

        // Evento click
        btnRegister.setOnClickListener(v -> {
            // Recoger datos
            RegisterRequest req = new RegisterRequest();
            req.nombre         = etNombre.getText().toString().trim();
            req.email          = etEmail.getText().toString().trim();
            req.password       = etPassword.getText().toString().trim();
            req.sexo           = spinnerSexo.getSelectedItem().toString();
            req.objetivo       = etObjetivo.getText().toString().trim();
            req.nivelActividad = ((NivelActividad)spinnerNivel.getSelectedItem()).toApiValue();

            try {
                req.edad   = Integer.parseInt(etEdad.getText().toString().trim());
                req.peso   = Double.parseDouble(etPeso.getText().toString().trim());
                req.altura = Double.parseDouble(etAltura.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Edad, peso o altura inválidos", Toast.LENGTH_LONG).show();
                return;
            }

            // Llamada al endpoint
            api.register(req).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> resp) {
                    if (resp.isSuccessful() && resp.body() != null) {
                        if (resp.body().success) {
                            Toast.makeText(RegisterActivity.this,
                                    "Registrado con ID=" + resp.body().idUsuario,
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    resp.body().message,
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // parsear ErrorResponse
                        try {
                            ErrorResponse err = new Gson().fromJson(
                                    resp.errorBody().charStream(), ErrorResponse.class);
                            Toast.makeText(RegisterActivity.this,
                                    err.message, Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            Toast.makeText(RegisterActivity.this,
                                    "Error en el registro", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,
                            "Fallo de conexión: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
//dfadad