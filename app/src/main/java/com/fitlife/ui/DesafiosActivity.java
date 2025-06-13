package com.fitlife.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlife.R;
import com.fitlife.ui.DesafioAdapter;
import com.fitlife.model.Desafio;
import com.fitlife.model.DesafiosResponse;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesafiosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DesafioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafios);

        recyclerView = findViewById(R.id.recyclerDesafios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarDesafios();
    }

    private void cargarDesafios() {
        FitLifeService api = RetrofitClient.getService();
        api.obtenerDesafios().enqueue(new Callback<DesafiosResponse>() {
            @Override
            public void onResponse(Call<DesafiosResponse> call, Response<DesafiosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Desafio> lista = response.body().getData();
                    adapter = new DesafioAdapter(lista);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(DesafiosActivity.this, "Error al cargar desafíos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DesafiosResponse> call, Throwable t) {
                Toast.makeText(DesafiosActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
