package com.fitlife.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fitlife.R;
import com.fitlife.model.Rutina;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RutinaAdapter extends RecyclerView.Adapter<RutinaAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClick(Rutina rutina);
    }

    private final List<Rutina> items = new ArrayList<>();
    private final OnClickListener listener;

    public RutinaAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Rutina> nuevas) {
        items.clear();
        if (nuevas != null) {
            items.addAll(nuevas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rutina, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rutina r = items.get(position);
        holder.tvNombre.setText(r.getNombre());
        holder.tvDescripcion.setText(r.getDescripcion());
        holder.tvNivel.setText("Nivel: " + r.getNivel());
        holder.itemView.setOnClickListener(v -> listener.onClick(r));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre;
        final TextView tvDescripcion;
        final TextView tvNivel;

        ViewHolder(View itemView) {
            super(itemView);
            tvNombre      = itemView.findViewById(R.id.tvItemNombre);
            tvDescripcion = itemView.findViewById(R.id.tvItemDescripcion);
            tvNivel       = itemView.findViewById(R.id.tvItemNivel);
        }
    }
}
