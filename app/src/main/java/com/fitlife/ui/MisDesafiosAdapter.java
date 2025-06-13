package com.fitlife.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.GenericResponse;
import com.fitlife.model.MisDesafio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisDesafiosAdapter extends RecyclerView.Adapter<MisDesafiosAdapter.MisDesafioViewHolder> {

    private final List<MisDesafio> lista;
    private final Context context;
    private final Runnable recargarCallback;

    public MisDesafiosAdapter(List<MisDesafio> lista, Context context, Runnable recargarCallback) {
        this.lista = lista;
        this.context = context;
        this.recargarCallback = recargarCallback;
    }

    @NonNull
    @Override
    public MisDesafioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mis_desafio, parent, false);
        return new MisDesafioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MisDesafioViewHolder holder, int position) {
        MisDesafio desafio = lista.get(position);

        holder.tvTitulo.setText(desafio.getTitulo());
        holder.tvDescripcion.setText(desafio.getDescripcion());
        holder.tvFechas.setText(desafio.getFechaInicio() + " - " + desafio.getFechaFin());

        // Completar
        if (desafio.isCompletado()) {
            holder.btnCompletar.setEnabled(false);
            holder.btnCompletar.setText("Completado");
        } else {
            holder.btnCompletar.setEnabled(true);
            holder.btnCompletar.setText("Completar");
            holder.btnCompletar.setOnClickListener(v -> {
                RetrofitClient.getService().completarDesafio(desafio.getDesafioId())
                        .enqueue(new Callback<GenericResponse>() {
                            @Override
                            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().success) {
                                    Toast.makeText(context, "Desafío completado", Toast.LENGTH_SHORT).show();
                                    recargarCallback.run();
                                } else {
                                    Toast.makeText(context, "Error al completar", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<GenericResponse> call, Throwable t) {
                                Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        }

        // Abandonar
        holder.btnAbandonar.setOnClickListener(v -> {
            RetrofitClient.getService().abandonarDesafio(desafio.getDesafioId())
                    .enqueue(new Callback<GenericResponse>() {
                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().success) {
                                Toast.makeText(context, "Desafío abandonado", Toast.LENGTH_SHORT).show();
                                recargarCallback.run();
                            } else {
                                Toast.makeText(context, "Error al abandonar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MisDesafioViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion, tvFechas;
        Button btnCompletar, btnAbandonar;

        public MisDesafioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo     = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFechas     = itemView.findViewById(R.id.tvFechas);
            btnCompletar = itemView.findViewById(R.id.btnCompletar);
            btnAbandonar = itemView.findViewById(R.id.btnAbandonar);
        }
    }
}
