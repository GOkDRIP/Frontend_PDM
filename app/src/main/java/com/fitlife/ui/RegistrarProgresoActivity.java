package com.fitlife.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.AgregarProgresoRequest;
import com.fitlife.model.GenericResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarProgresoActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private EditText etFecha, etPeso, etCalorias, etObservaciones;
    private Button btnEnviar;
    private FitLifeService api;
    private final SimpleDateFormat sdf =
            new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_progreso);

        etFecha         = findViewById(R.id.et_fecha);
        etPeso          = findViewById(R.id.et_peso);
        etCalorias      = findViewById(R.id.et_calorias);
        etObservaciones = findViewById(R.id.et_observaciones);
        btnEnviar       = findViewById(R.id.btn_enviar_progreso);

        api = RetrofitClient.getService();

        // Inicializamos el campo fecha con la fecha de hoy
        Calendar cal = Calendar.getInstance();
        etFecha.setText(sdf.format(cal.getTime()));

        // Al tocar el campo de fecha, abrimos el DatePicker
        etFecha.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(etFecha.getText().toString()));
            } catch (Exception ignore) {}
            new DatePickerDialog(
                    RegistrarProgresoActivity.this,
                    (DatePicker dp, int y, int m, int d) -> {
                        Calendar sel = Calendar.getInstance();
                        sel.set(y, m, d);
                        etFecha.setText(sdf.format(sel.getTime()));
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnEnviar.setOnClickListener(v -> enviarProgreso());
    }

    private void enviarProgreso() {
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

        AgregarProgresoRequest req =
                new AgregarProgresoRequest(fecha, peso, calorias, observaciones);

        api.agregarProgreso(req).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {
                // Si la sesión expiró, volvemos al login
                if (response.code() == 401) {
                    Toast.makeText(
                            RegistrarProgresoActivity.this,
                            "Sesión expirada, vuelve a iniciar sesión.",
                            Toast.LENGTH_LONG
                    ).show();
                    startActivity(new Intent(
                            RegistrarProgresoActivity.this,
                            LoginActivity.class
                    ));
                    finish();          // <- exacto igual que antes
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    String msg = response.body().message != null
                            ? response.body().message
                            : "Progreso registrado correctamente.";
                    Toast.makeText(
                            RegistrarProgresoActivity.this,
                            msg,
                            Toast.LENGTH_LONG
                    ).show();
                    setResult(RESULT_OK);  // <- si lo llamaste con forResult
                    finish();             // <- cierre como antes
                } else {
                    Toast.makeText(
                            RegistrarProgresoActivity.this,
                            "Error al enviar progreso",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(
                        RegistrarProgresoActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
