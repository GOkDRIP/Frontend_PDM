// File: FeedAdapter.java
package com.fitlife.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.fitlife.R;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.model.ComidaFeedItem;
import com.fitlife.model.GenericResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private static final String BASE_URL = "http://13.61.161.23:8080/fitlife/";
    private List<ComidaFeedItem> items;

    public FeedAdapter(List<ComidaFeedItem> items) {
        this.items = items;
    }

    public void updateItems(List<ComidaFeedItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feed_comida, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComidaFeedItem item = items.get(position);
        holder.tvUsuario.setText(item.getUsuarioNombre());
        holder.tvNombre.setText(item.getNombreComida());
        holder.tvMacros.setText(
                item.getCalorias() + " kcal | C " + item.getCarbohidratos() +
                        "g P " + item.getProteinas() + "g G " + item.getGrasas() + "g"
        );
        holder.tvDescripcion.setText(item.getDescripcion());
        holder.tvFecha.setText(item.getFechaPublicacion());

        Glide.with(holder.ivFoto.getContext())
                .load(BASE_URL + "uploads/" + item.getFotoURL())
                .into(holder.ivFoto);

        holder.tvLikes.setText(String.valueOf(item.getLikes()));
        holder.btnLike.setImageResource(
                item.isHaDadoLike() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
        );

        // Like / Unlike
        holder.btnLike.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            ComidaFeedItem clicked = items.get(pos);
            FitLifeService service = RetrofitClient.getService();
            Call<GenericResponse> call = clicked.isHaDadoLike()
                    ? service.unlike(clicked.getIdPublicacion())
                    : service.like(clicked.getIdPublicacion());

            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(@NonNull Call<GenericResponse> call,
                                       @NonNull Response<GenericResponse> response) {
                    GenericResponse body = response.body();
                    if (body != null && body.success) {
                        boolean nuevoEstado = !clicked.isHaDadoLike();
                        clicked.setHaDadoLike(nuevoEstado);
                        clicked.setLikes(clicked.getLikes() + (nuevoEstado ? 1 : -1));
                        notifyItemChanged(pos);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GenericResponse> call,
                                      @NonNull Throwable t) {
                    // Opcional: manejar error de red
                }
            });
        });

        // Habilitar long click y mostrar diálogo de confirmación para eliminar
        holder.itemView.setLongClickable(true);
        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return true;
            ComidaFeedItem toDelete = items.get(pos);

            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Eliminar publicación")
                    .setMessage("¿Seguro que quieres eliminar esta publicación?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        FitLifeService service = RetrofitClient.getService();
                        service.eliminarComida(toDelete.getIdPublicacion())
                                .enqueue(new Callback<GenericResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<GenericResponse> call,
                                                           @NonNull Response<GenericResponse> response) {
                                        GenericResponse body = response.body();
                                        if (body != null && body.success) {
                                            items.remove(pos);
                                            notifyItemRemoved(pos);
                                            Toast.makeText(
                                                    holder.itemView.getContext(),
                                                    body.message,
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        } else {
                                            Toast.makeText(
                                                    holder.itemView.getContext(),
                                                    body != null
                                                            ? body.message
                                                            : "Error al eliminar",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<GenericResponse> call,
                                                          @NonNull Throwable t) {
                                        Toast.makeText(
                                                holder.itemView.getContext(),
                                                "Fallo de red al eliminar",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvUsuario, tvNombre, tvMacros, tvDescripcion, tvFecha, tvLikes;
        ImageButton btnLike;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto        = itemView.findViewById(R.id.ivFoto);
            tvUsuario     = itemView.findViewById(R.id.tvUsuario);
            tvNombre      = itemView.findViewById(R.id.tvNombreComida);
            tvMacros      = itemView.findViewById(R.id.tvMacros);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFecha       = itemView.findViewById(R.id.tvFecha);
            tvLikes       = itemView.findViewById(R.id.tvLikes);
            btnLike       = itemView.findViewById(R.id.btnLike);
        }
    }
}
