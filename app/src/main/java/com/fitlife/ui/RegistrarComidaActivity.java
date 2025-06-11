package com.fitlife.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.GenericResponse;
import com.fitlife.model.RegistrarComidaRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarComidaActivity extends AppCompatActivity {

    private EditText etNombre,
            etCalorias,
            etCarbs,
            etProteinas,
            etGrasas,
            etObservaciones;
    private Button btnGuardar;
    private FitLifeService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_comida);

        // Vincular vistas
        etNombre        = findViewById(R.id.et_nombre_comida);
        etCalorias      = findViewById(R.id.et_calorias_comida);
        etCarbs         = findViewById(R.id.et_carbohidratos_comida);
        etProteinas     = findViewById(R.id.et_proteinas_comida);
        etGrasas        = findViewById(R.id.et_grasas_comida);
        etObservaciones = findViewById(R.id.et_observaciones_comida);
        btnGuardar      = findViewById(R.id.btn_guardar_comida);

        // Servicio Retrofit
        api = RetrofitClient.getService();

        btnGuardar.setOnClickListener(v -> registrarComida());
    }

    private void registrarComida() {
        String nombre    = etNombre.getText().toString().trim();
        String calStr    = etCalorias.getText().toString().trim();
        String carbStr   = etCarbs.getText().toString().trim();
        String protStr   = etProteinas.getText().toString().trim();
        String grasaStr  = etGrasas.getText().toString().trim();
        String obs       = etObservaciones.getText().toString().trim();

        if (nombre.isEmpty() || calStr.isEmpty()) {
            Toast.makeText(this,
                            "Por favor, ingresa al menos nombre y calorías",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        int calorias;
        double carbs, proteinas, grasas;
        try {
            calorias  = Integer.parseInt(calStr);
            carbs     = carbStr.isEmpty()   ? 0.0 : Double.parseDouble(carbStr);
            proteinas = protStr.isEmpty()   ? 0.0 : Double.parseDouble(protStr);
            grasas    = grasaStr.isEmpty()  ? 0.0 : Double.parseDouble(grasaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                            "Calorías, carbohidratos, proteínas y grasas deben ser numéricos",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        RegistrarComidaRequest req = new RegistrarComidaRequest(
                nombre, calorias, carbs, proteinas, grasas, obs
        );

        api.registrarComida(req).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {
                if (response.code() == 401) {
                    // Sesión caducada
                    Toast.makeText(RegistrarComidaActivity.this,
                                    "Sesión expirada, inicia sesión de nuevo",
                                    Toast.LENGTH_LONG)
                            .show();
                    setResult(RESULT_CANCELED);
                    finish();
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse gr = response.body();
                    // Aseguramos que message no sea null
                    String msg = gr.message != null
                            ? gr.message
                            : "Comida registrada";
                    Toast.makeText(RegistrarComidaActivity.this,
                                    msg,
                                    Toast.LENGTH_LONG)
                            .show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String err = "Error al registrar comida";
                    if (response.errorBody() != null) {
                        err += ": " + response.code();
                    }
                    Toast.makeText(RegistrarComidaActivity.this,
                                    err,
                                    Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(RegistrarComidaActivity.this,
                                "Fallo de red: " + t.getMessage(),
                                Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}
