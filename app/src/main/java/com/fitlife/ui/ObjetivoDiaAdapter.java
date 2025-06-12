package com.fitlife.ui;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fitlife.R;
import com.fitlife.model.ObjetivoDiaResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ObjetivoDiaAdapter
        extends RecyclerView.Adapter<ObjetivoDiaAdapter.ViewHolder> {

    public interface OnCompleteListener {
        void onComplete(ObjetivoDiaResponse objetivo);
    }

    private final List<ObjetivoDiaResponse> items = new ArrayList<>();
    private final Set<Integer> completedIds = new HashSet<>();
    private final OnCompleteListener listener;

    public ObjetivoDiaAdapter(OnCompleteListener listener) {
        this.listener = listener;
    }

    public void updateData(List<ObjetivoDiaResponse> nuevas) {
        items.clear();
        if (nuevas != null) items.addAll(nuevas);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_objetivo_dia, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ObjetivoDiaResponse obj = items.get(pos);
        h.tvDescripcion.setText(obj.getDescripcion());

        boolean done = completedIds.contains(obj.getId());
        // Marcar tachado si ya completado
        if (done) {
            h.tvDescripcion.setPaintFlags(
                    h.tvDescripcion.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            h.btnCompletar.setEnabled(false);
        } else {
            h.tvDescripcion.setPaintFlags(
                    h.tvDescripcion.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
            );
            h.btnCompletar.setEnabled(true);
        }

        h.btnCompletar.setOnClickListener(v -> {
            completedIds.add(obj.getId());
            notifyItemChanged(pos);
            listener.onComplete(obj);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDescripcion;
        final Button btnCompletar;
        ViewHolder(View v) {
            super(v);
            tvDescripcion  = v.findViewById(R.id.tvDescripcionObjetivo);
            btnCompletar   = v.findViewById(R.id.btnCompletarObjetivo);
        }
    }
}
