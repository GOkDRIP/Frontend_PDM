package com.fitlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.fitlife.R;
import com.fitlife.utils.SessionManager;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* ⬇️ Comprueba si ya hay sesión */
        SessionManager sm = new SessionManager(getApplicationContext());
        if (sm.isLoggedIn()) {
            startActivity(new Intent(this, EntrenamientoActivity.class));
            finish();   // Saltamos la pantalla de bienvenida/login
            return;
        }

        setContentView(R.layout.activity_welcome);

        Button btnLogin    = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}
