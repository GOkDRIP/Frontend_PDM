package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.ComidaResponse;
import com.fitlife.model.ListarComidasResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ListarComidasActivity extends AppCompatActivity {
    private static final int REQUEST_REGISTRAR = 100;
    private TextView tvResumen;
    private RecyclerView rvComidas;
    private Button btnAgregar;
    private FitLifeService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_comidas);

        tvResumen  = findViewById(R.id.tvResumen);
        rvComidas  = findViewById(R.id.rvComidas);
        btnAgregar = findViewById(R.id.btnAgregarComida);

        rvComidas.setLayoutManager(new LinearLayoutManager(this));
        api = RetrofitClient.getService();

        // 1) Lanzamos RegistrarComidaActivity esperando RESULT_OK
        btnAgregar.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrarComidaActivity.class);
            startActivityForResult(i, REQUEST_REGISTRAR);
        });

        // 2) Cargamos la lista la primera vez
        cargarComidas();
    }

    // 3) Si volvemos de RegistrarComida con resultado OK, recargamos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTRAR && resultCode == RESULT_OK) {
            cargarComidas();
        }
    }

    private void cargarComidas() {
        api.listarComidas().enqueue(new Callback<ListarComidasResponse>() {
            @Override
            public void onResponse(Call<ListarComidasResponse> call,
                                   Response<ListarComidasResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null && resp.body().success) {
                    List<ComidaResponse> lista = resp.body().comidas;
                    rvComidas.setAdapter(new ComidaAdapter(lista));
                    tvResumen.setText(
                            "Has consumido " +
                                    resp.body().totalCalorias +
                                    " kcal de " +
                                    resp.body().caloriasRec +
                                    " recomendadas."
                    );
                } else {
                    String msg = (resp.body() != null)
                            ? resp.body().message
                            : "Error al listar comidas";
                    Toast.makeText(ListarComidasActivity.this,
                            msg,
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ListarComidasResponse> call, Throwable t) {
                Toast.makeText(ListarComidasActivity.this,
                        "Fallo de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
