package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.R;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.model.LoginRequest;
import com.fitlife.model.LoginResponse;
import com.fitlife.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FitLifeService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        api = RetrofitClient.getService();

        Button registerLink = findViewById(R.id.btnRegister);
        TextView forgotPasswordLink = findViewById(R.id.forgotPassword);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();
            login(email, pass);
        });

        registerLink.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        forgotPasswordLink.setOnClickListener(v ->
                startActivity(new Intent(this, RecuperarContrasenaActivity.class))
        );
    }

    private void login(String email, String password) {
        LoginRequest req = new LoginRequest(email, password);
        api.login(req).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null && resp.body().success) {

                    /* ⬇️ Guarda la sesión */
                    new SessionManager(getApplicationContext())
                            .login(email, password);

                    Toast.makeText(LoginActivity.this,
                            "Bienvenido " + resp.body().nombre,
                            Toast.LENGTH_LONG).show();

                    startActivity(new Intent(LoginActivity.this, EntrenamientoActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Error: credenciales inválidas",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Fallo conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
