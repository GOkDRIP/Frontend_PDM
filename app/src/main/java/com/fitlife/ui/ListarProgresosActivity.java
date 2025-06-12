package com.fitlife.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlife.R;
import com.fitlife.ui.ProgresoAdapter;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.ListarProgresosResponse;
import com.fitlife.model.Progreso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarProgresosActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private TextView tvDesde, tvHasta;
    private Button btnFiltrar;
    private ProgressBar progressBar;
    private RecyclerView recyclerProgresos;
    private ProgresoAdapter adapter;

    private FitLifeService api;
    private final SimpleDateFormat sdf =
            new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_progresos);

        // Vistas
        tvDesde          = findViewById(R.id.tvDesde);
        tvHasta          = findViewById(R.id.tvHasta);
        btnFiltrar       = findViewById(R.id.btnFiltrar);
        progressBar      = findViewById(R.id.progressBar);
        recyclerProgresos = findViewById(R.id.recyclerProgresos);

        // RecyclerView + Adapter
        adapter = new ProgresoAdapter(new java.util.ArrayList<>());
        recyclerProgresos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProgresos.setAdapter(adapter);

        // Retrofit + cookies/logging vienen de RetrofitClient
        api = RetrofitClient.getService();

        // Inicializar filtros con fechas por defecto
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        tvHasta.setText(sdf.format(today));

        cal.add(Calendar.WEEK_OF_YEAR, -1);
        Date weekAgo = cal.getTime();
        tvDesde.setText(sdf.format(weekAgo));

        // DatePickerDialogs
        tvDesde.setOnClickListener(v -> showDatePicker(tvDesde));
        tvHasta.setOnClickListener(v -> showDatePicker(tvHasta));

        // Filtrar al presionar botón
        btnFiltrar.setOnClickListener(v -> loadProgresos(
                tvDesde.getText().toString(),
                tvHasta.getText().toString()
        ));

        // Carga inicial
        loadProgresos(tvDesde.getText().toString(),
                tvHasta.getText().toString());
    }

    private void showDatePicker(TextView target) {
        Calendar cal = Calendar.getInstance();
        try {
            Date parsed = sdf.parse(target.getText().toString());
            cal.setTime(parsed);
        } catch (Exception ignored) { }

        int y = cal.get(Calendar.YEAR),
                m = cal.get(Calendar.MONTH),
                d = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int day) -> {
                    Calendar sel = Calendar.getInstance();
                    sel.set(year, month, day);
                    target.setText(sdf.format(sel.getTime()));
                },
                y, m, d
        ).show();
    }

    private void loadProgresos(String desde, String hasta) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        api.listarProgresos(desde, hasta)
                .enqueue(new Callback<ListarProgresosResponse>() {
                    @Override
                    public void onResponse(Call<ListarProgresosResponse> call,
                                           Response<ListarProgresosResponse> resp) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        if (resp.isSuccessful() && resp.body() != null) {
                            ListarProgresosResponse body = resp.body();
                            if (body.isSuccess()) {
                                List<Progreso> lista = body.getProgresos();
                                adapter.updateData(lista);
                            } else {
                                Toast.makeText(
                                        ListarProgresosActivity.this,
                                        body.getMessage() != null
                                                ? body.getMessage()
                                                : "Error al listar",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } else {
                            Toast.makeText(
                                    ListarProgresosActivity.this,
                                    "Respuesta inválida del servidor",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ListarProgresosResponse> call,
                                          Throwable t) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        Toast.makeText(
                                ListarProgresosActivity.this,
                                "Fallo de red: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
