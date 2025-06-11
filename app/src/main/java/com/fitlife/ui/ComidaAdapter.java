package com.fitlife.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fitlife.R;
import com.fitlife.model.ComidaResponse;
import java.util.List;

public class ComidaAdapter extends RecyclerView.Adapter<ComidaAdapter.VH> {
    private final List<ComidaResponse> lista;
    public ComidaAdapter(List<ComidaResponse> lista) { this.lista = lista; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comida, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ComidaResponse c = lista.get(position);
        holder.tvNombre.setText(c.nombre);
        holder.tvCalorias.setText(c.calorias + " kcal");
        holder.tvCarbs  .setText(c.carbohidratos + " g");
        holder.tvProt   .setText(c.proteinas + " g");
        holder.tvGrasas .setText(c.grasas + " g");
    }

    @Override public int getItemCount() { return lista.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCalorias, tvCarbs, tvProt, tvGrasas;
        VH(View v) {
            super(v);
            tvNombre   = v.findViewById(R.id.tvNombre);
            tvCalorias = v.findViewById(R.id.tvCalorias);
            tvCarbs    = v.findViewById(R.id.tvCarbs);
            tvProt     = v.findViewById(R.id.tvProt);
            tvGrasas   = v.findViewById(R.id.tvGrasas);
        }
    }
}
