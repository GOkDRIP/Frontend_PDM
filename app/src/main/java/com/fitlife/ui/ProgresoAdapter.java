package com.fitlife.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlife.model.Progreso;
import com.fitlife.R;

import java.util.List;

public class ProgresoAdapter extends RecyclerView.Adapter<ProgresoAdapter.ViewHolder> {

    private List<Progreso> data;

    public ProgresoAdapter(List<Progreso> data) {
        this.data = data;
    }

    public void updateData(List<Progreso> nuevosProgresos) {
        this.data = nuevosProgresos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProgresoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_progreso, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgresoAdapter.ViewHolder holder, int position) {
        Progreso p = data.get(position);
        holder.tvFecha.setText("Fecha: " + p.getFecha());
        holder.tvPeso.setText("Peso: " + p.getPeso() + " kg");
        holder.tvCalorias.setText("Calorías: " + p.getCalorias());
        holder.tvObservaciones.setText("Obs.: " + p.getObservaciones());
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvPeso, tvCalorias, tvObservaciones;

        ViewHolder(View itemView) {
            super(itemView);
            tvFecha         = itemView.findViewById(R.id.tvFecha);
            tvPeso          = itemView.findViewById(R.id.tvPeso);
            tvCalorias      = itemView.findViewById(R.id.tvCalorias);
            tvObservaciones = itemView.findViewById(R.id.tvObservaciones);
        }
    }
}
