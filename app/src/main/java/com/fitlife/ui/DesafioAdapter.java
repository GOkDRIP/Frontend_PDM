package com.fitlife.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlife.R;
import com.fitlife.model.Desafio;

import java.util.List;

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

        // Formatea las fechas (asumimos que son tipo String)
        String fechas = desafio.getFechaInicio() + " - " + desafio.getFechaFin();
        holder.tvFechas.setText(fechas);
    }

    @Override
    public int getItemCount() {
        return listaDesafios.size();
    }

    static class DesafioViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvDescripcion, tvFechas;

        public DesafioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFechas = itemView.findViewById(R.id.tvFechas);
        }
    }
}
