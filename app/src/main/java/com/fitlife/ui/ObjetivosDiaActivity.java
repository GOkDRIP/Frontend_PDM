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
import com.fitlife.model.ObjetivosDiaResponse;
import com.fitlife.model.ObjetivoDiaResponse;
import com.fitlife.ui.ObjetivoDiaAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObjetivosDiaActivity extends AppCompatActivity {

    private RecyclerView rvObjetivos;
    private ProgressBar prog;
    private FitLifeService api;
    private ObjetivoDiaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivos_dia);

        rvObjetivos = findViewById(R.id.rvObjetivos);
        prog        = findViewById(R.id.progObjetivos);

        api = RetrofitClient.getService();
        adapter = new ObjetivoDiaAdapter(obj ->
                Toast.makeText(this, "Objetivo completado", Toast.LENGTH_SHORT).show()
        );
        rvObjetivos.setLayoutManager(new LinearLayoutManager(this));
        rvObjetivos.setAdapter(adapter);

        loadObjetivosDia();
    }

    private void loadObjetivosDia() {
        prog.setVisibility(ProgressBar.VISIBLE);
        api.getObjetivosDia().enqueue(new Callback<ObjetivosDiaResponse>() {
            @Override
            public void onResponse(Call<ObjetivosDiaResponse> call,
                                   Response<ObjetivosDiaResponse> resp) {
                prog.setVisibility(ProgressBar.GONE);
                if (resp.isSuccessful() && resp.body() != null) {
                    ObjetivosDiaResponse b = resp.body();
                    if (b.isSuccess()) {
                        List<ObjetivoDiaResponse> list = b.getData();
                        adapter.updateData(list);
                    } else {
                        Toast.makeText(ObjetivosDiaActivity.this, b.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ObjetivosDiaActivity.this, "Error en servidor", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ObjetivosDiaResponse> call, Throwable t) {
                prog.setVisibility(ProgressBar.GONE);
                Toast.makeText(ObjetivosDiaActivity.this, "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
