package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.model.RecuperarContrasenaRequest;
import com.fitlife.model.RecuperarContrasenaResponse;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button recuperarBtn;
    private Button btnIrARestablecer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        emailEditText = findViewById(R.id.emailInput);
        recuperarBtn = findViewById(R.id.recuperarBtn);
        btnIrARestablecer = findViewById(R.id.btnIrARestablecer);

        // Inicialmente oculto
        btnIrARestablecer.setVisibility(View.GONE);

        recuperarBtn.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Introduce un email", Toast.LENGTH_SHORT).show();
                return;
            }

            RecuperarContrasenaRequest request = new RecuperarContrasenaRequest(email);

            FitLifeService service = RetrofitClient.getService();
            service.recuperarContrasena(request).enqueue(new Callback<RecuperarContrasenaResponse>() {
                @Override
                public void onResponse(Call<RecuperarContrasenaResponse> call, Response<RecuperarContrasenaResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RecuperarContrasenaActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        // Si fue exitoso, mostrar el botón
                        if (response.body().isSuccess()) {
                            btnIrARestablecer.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(RecuperarContrasenaActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RecuperarContrasenaResponse> call, Throwable t) {
                    Toast.makeText(RecuperarContrasenaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnIrARestablecer.setOnClickListener(v -> {
            Intent intent = new Intent(RecuperarContrasenaActivity.this, RestablecerContrasenaActivity.class);
            startActivity(intent);
        });
    }
}
