package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.AgregarProgresoRequest;
import com.fitlife.model.GenericResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarProgresoActivity extends AppCompatActivity {

    private EditText etFecha, etPeso, etCalorias, etObservaciones;
    private Button btnEnviar;
    private FitLifeService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_progreso);

        // Bind de vistas
        etFecha         = findViewById(R.id.et_fecha);
        etPeso          = findViewById(R.id.et_peso);
        etCalorias      = findViewById(R.id.et_calorias);
        etObservaciones = findViewById(R.id.et_observaciones);
        btnEnviar       = findViewById(R.id.btn_enviar_progreso);

        // Inicializar el servicio (usa tu RetrofitClient con CookieJar)
        api = RetrofitClient.getService();

        btnEnviar.setOnClickListener(v -> enviarProgreso());
    }

    private void enviarProgreso() {
        // Validaciones
        String fecha = etFecha.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();
        String caloriasStr = etCalorias.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        if (fecha.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa la fecha.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pesoStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa el peso.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (caloriasStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa las calorías.", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso;
        int calorias;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El peso debe ser un número válido.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            calorias = Integer.parseInt(caloriasStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Las calorías deben ser un número válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos la petición
        AgregarProgresoRequest req =
                new AgregarProgresoRequest(fecha, peso, calorias, observaciones);

        // Llamada al servidor con cookie de sesión
        api.agregarProgreso(req).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {
                if (response.code() == 401) {
                    // Sesión caducada: volvemos al login
                    Toast.makeText(RegistrarProgresoActivity.this,
                            "Sesión expirada, por favor vuelve a iniciar sesión.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegistrarProgresoActivity.this,
                            LoginActivity.class));
                    finish();
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegistrarProgresoActivity.this,
                            response.body().message,
                            Toast.LENGTH_LONG).show();
                    // Al cerrar, volverá a EntrenamientoActivity con la cookie intacta
                    finish();
                } else {
                    Toast.makeText(RegistrarProgresoActivity.this,
                            "Error al enviar progreso",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(RegistrarProgresoActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
