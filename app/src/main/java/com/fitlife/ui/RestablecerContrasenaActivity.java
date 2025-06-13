package com.fitlife.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.model.RestablecerContrasenaRequest;
import com.fitlife.model.RestablecerContrasenaResponse;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class RestablecerContrasenaActivity extends AppCompatActivity {

    private EditText tokenEditText, nuevaPwdEditText;
    private Button restablecerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_contrasena);

        tokenEditText = findViewById(R.id.tokenInput);
        nuevaPwdEditText = findViewById(R.id.newPasswordInput);
        restablecerBtn = findViewById(R.id.restablecerBtn);

        restablecerBtn.setOnClickListener(v -> {
            String token = tokenEditText.getText().toString().trim();
            String nuevaPwd = nuevaPwdEditText.getText().toString().trim();

            if (token.isEmpty() || nuevaPwd.isEmpty()) {
                Toast.makeText(this, "Rellena ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            RestablecerContrasenaRequest request = new RestablecerContrasenaRequest(token, nuevaPwd);

            FitLifeService service = RetrofitClient.getService();
            service.restablecerContrasena(request).enqueue(new Callback<RestablecerContrasenaResponse>() {
                @Override
                public void onResponse(Call<RestablecerContrasenaResponse> call, Response<RestablecerContrasenaResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RestablecerContrasenaActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        if (response.body().isSuccess()) {
                            // ✅ Redirigir a LoginActivity
                            Intent intent = new Intent(RestablecerContrasenaActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Cierra esta pantalla
                        }
                    } else {
                        Toast.makeText(RestablecerContrasenaActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<RestablecerContrasenaResponse> call, Throwable t) {
                    Toast.makeText(RestablecerContrasenaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
