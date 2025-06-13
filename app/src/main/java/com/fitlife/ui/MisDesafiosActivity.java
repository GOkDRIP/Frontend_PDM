package com.fitlife.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlife.R;
import com.fitlife.ui.MisDesafiosAdapter;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.MisDesafio;
import com.fitlife.model.MisDesafiosResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisDesafiosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FitLifeService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_desafios);

        recyclerView = findViewById(R.id.recyclerMisDesafios);
        progressBar = findViewById(R.id.progressBarMisDesafios);
        service = RetrofitClient.getService();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cargarMisDesafios();
    }

    private void cargarMisDesafios() {
        progressBar.setVisibility(View.VISIBLE);

        service.getMisDesafios().enqueue(new Callback<MisDesafiosResponse>() {
            @Override
            public void onResponse(Call<MisDesafiosResponse> call, Response<MisDesafiosResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    List<MisDesafio> lista = response.body().data;

                    MisDesafiosAdapter adapter = new MisDesafiosAdapter(lista, MisDesafiosActivity.this, () -> cargarMisDesafios());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MisDesafiosActivity.this, "Error al cargar desafíos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MisDesafiosResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MisDesafiosActivity.this, "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
