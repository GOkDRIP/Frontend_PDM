package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.Rutina;
import com.fitlife.model.VerRutinaActualResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerRutinaActualActivity extends AppCompatActivity {
    private static final int REQ_ASIGNAR = 3001;

    private TextView tvNombre, tvDescripcion, tvNivel;
    private ProgressBar prog;
    private Button btnElegir;
    private FitLifeService api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_rutina_actual);

        tvNombre      = findViewById(R.id.tvNombreRutina);
        tvDescripcion = findViewById(R.id.tvDescripcionRutina);
        tvNivel       = findViewById(R.id.tvNivelRutina);
        prog          = findViewById(R.id.progVerActual);
        btnElegir     = findViewById(R.id.btnElegirRutina);

        api = RetrofitClient.getService();

        btnElegir.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, AsignarRutinaActivity.class),
                        REQ_ASIGNAR
                )
        );

        loadRutinaActual();
    }

    private void loadRutinaActual() {
        prog.setVisibility(View.VISIBLE);
        api.getVerRutinaActual().enqueue(new Callback<VerRutinaActualResponse>() {
            @Override
            public void onResponse(Call<VerRutinaActualResponse> call, Response<VerRutinaActualResponse> resp) {
                prog.setVisibility(View.GONE);
                if (resp.isSuccessful() && resp.body() != null) {
                    VerRutinaActualResponse body = resp.body();
                    if (body.isSuccess() && !body.getData().isEmpty()) {
                        Rutina r = body.getData().get(0);
                        tvNombre.setText(r.getNombre());
                        tvDescripcion.setText(r.getDescripcion());
                        tvNivel.setText("Nivel: " + r.getNivel());
                    } else {
                        Toast.makeText(VerRutinaActualActivity.this,
                                body.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerRutinaActualActivity.this,
                            "Error en servidor",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<VerRutinaActualResponse> call, Throwable t) {
                prog.setVisibility(View.GONE);
                Toast.makeText(VerRutinaActualActivity.this,
                        "Fallo de red",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ASIGNAR && resultCode == RESULT_OK) {
            // Si se asignó exitosamente, recargamos la rutina actual
            loadRutinaActual();
        }
    }
}
