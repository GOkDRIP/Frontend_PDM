package com.fitlife.ui;

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
import com.fitlife.model.Desafio;
import com.fitlife.model.GenericResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesafioAdapter extends RecyclerView.Adapter<DesafioAdapter.DesafioViewHolder> {

    private final List<Desafio> listaDesafios;

    public DesafioAdapter(List<Desafio> listaDesafios) {
        this.listaDesafios = listaDesafios;
    }

    @NonNull
    @Override
    public DesafioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_desafio, parent, false);
        return new DesafioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull DesafioViewHolder holder, int position) {
        Desafio desafio = listaDesafios.get(position);

        holder.tvTitulo.setText(desafio.getTitulo());
        holder.tvDescripcion.setText(desafio.getDescripcion());

        String fechas = desafio.getFechaInicio() + " - " + desafio.getFechaFin();
        holder.tvFechas.setText(fechas);

        if (desafio.isYaUnido()) {
            holder.btnUnirse.setEnabled(false);
            holder.btnUnirse.setText("Ya unido");
        } else {
            holder.btnUnirse.setEnabled(true);
            holder.btnUnirse.setText("Unirse");

            holder.btnUnirse.setOnClickListener(view -> {
                Map<String, Integer> body = new HashMap<>();
                body.put("desafioId", desafio.getId());

                FitLifeService service = RetrofitClient.getService();
                service.unirseDesafio(body).enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().success) {
                            Toast.makeText(view.getContext(), response.body().message, Toast.LENGTH_SHORT).show();
                            holder.btnUnirse.setEnabled(false);
                            holder.btnUnirse.setText("Ya unido");
                            desafio.setYaUnido(true); // actualiza el modelo también
                        } else {
                            Toast.makeText(view.getContext(), "No se pudo unir al desafío", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaDesafios.size();
    }

    static class DesafioViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvDescripcion, tvFechas;
        Button btnUnirse;

        public DesafioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo     = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFechas     = itemView.findViewById(R.id.tvFechas);
            btnUnirse    = itemView.findViewById(R.id.btnUnirse);
        }
    }
}
