// File: FeedActivity.java
package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.fitlife.R;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.model.ComidaFeedResponse;
import com.fitlife.model.ComidaFeedItem;
import com.fitlife.model.GenericResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Collections;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private static final int REQUEST_POST = 123;
    private RecyclerView rvFeed;
    private FeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvFeed = findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedAdapter(Collections.emptyList());
        rvFeed.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddPost);
        fab.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(FeedActivity.this, NuevaComidaActivity.class),
                        REQUEST_POST
                )
        );

        loadFeed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POST && resultCode == RESULT_OK) {
            loadFeed();
        }
    }

    private void loadFeed() {
        FitLifeService service = RetrofitClient.getService();
        service.getFeed().enqueue(new Callback<ComidaFeedResponse>() {
            @Override
            public void onResponse(@NonNull Call<ComidaFeedResponse> call,
                                   @NonNull Response<ComidaFeedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ComidaFeedResponse body = response.body();
                    if (body.isSuccess()) {
                        List<ComidaFeedItem> items = body.getData();
                        adapter.updateItems(items);
                    } else {
                        Toast.makeText(FeedActivity.this,
                                "Error cargando el feed",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FeedActivity.this,
                            "Error HTTP: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ComidaFeedResponse> call,
                                  @NonNull Throwable t) {
                Toast.makeText(FeedActivity.this,
                        "Fallo de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
