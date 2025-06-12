package com.fitlife.ui;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.AsignarRutinaResponse;
import com.fitlife.model.Rutina;
import com.fitlife.model.RutinaRequest;
import com.fitlife.ui.RutinaAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsignarRutinaActivity extends AppCompatActivity {

    private RecyclerView rvRutinas;
    private ProgressBar prog;
    private FitLifeService api;
    private RutinaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1) Asigna el layout de la Activity
        setContentView(R.layout.activity_asignar_rutina);

        // 2) Sin 'view', busca directamente las vistas por su id
        rvRutinas = findViewById(R.id.rvRutinas);
        prog       = findViewById(R.id.progAsignar);

        api = RetrofitClient.getService();

        adapter = new RutinaAdapter(rutina -> asignarRutina(rutina.getId()));
        rvRutinas.setLayoutManager(new LinearLayoutManager(this));
        rvRutinas.setAdapter(adapter);

        loadTodasLasRutinas();
    }

    private void loadTodasLasRutinas() {
        prog.setVisibility(ProgressBar.VISIBLE);
        api.getTodasLasRutinas().enqueue(new Callback<List<Rutina>>() {
            @Override
            public void onResponse(Call<List<Rutina>> call, Response<List<Rutina>> resp) {
                prog.setVisibility(ProgressBar.GONE);
                if (resp.isSuccessful() && resp.body() != null) {
                    adapter.updateData(resp.body());
                } else {
                    Toast.makeText(AsignarRutinaActivity.this,
                            "Error al cargar rutinas",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Rutina>> call, Throwable t) {
                prog.setVisibility(ProgressBar.GONE);
                Toast.makeText(AsignarRutinaActivity.this,
                        "Fallo de red",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asignarRutina(long rutinaId) {
        prog.setVisibility(ProgressBar.VISIBLE);
        api.postAsignarRutina(new RutinaRequest(rutinaId))
                .enqueue(new Callback<AsignarRutinaResponse>() {
                    @Override
                    public void onResponse(Call<AsignarRutinaResponse> call,
                                           Response<AsignarRutinaResponse> resp) {
                        prog.setVisibility(ProgressBar.GONE);
                        if (resp.isSuccessful() && resp.body() != null && resp.body().isSuccess()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            String msg = resp.body() != null
                                    ? resp.body().getMessage()
                                    : "Error en servidor";
                            Toast.makeText(AsignarRutinaActivity.this,
                                    msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<AsignarRutinaResponse> call, Throwable t) {
                        prog.setVisibility(ProgressBar.GONE);
                        Toast.makeText(AsignarRutinaActivity.this,
                                "Fallo de red",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
