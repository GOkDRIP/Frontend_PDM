package com.fitlife.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Encapsula el acceso a SharedPreferences para mantener la sesión.
 */
public class SessionManager {

    private static final String PREF_NAME     = "FitLifePrefs";
    private static final String KEY_EMAIL     = "email";
    private static final String KEY_PASSWORD  = "password";   // (solo la usas para el endpoint de eliminar)
    private static final String KEY_LOGGED_IN = "logged_in";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context ctx) {
        prefs  = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /** Guarda las credenciales y marca la sesión como iniciada */
    public void login(String email, String password) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.apply();
    }

    /** Borra toda la información de sesión */
    public void logout() {
        editor.clear().apply();
    }

    /** Devuelve true si el usuario sigue logueado */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public String getEmail()    { return prefs.getString(KEY_EMAIL, null); }
    public String getPassword() { return prefs.getString(KEY_PASSWORD, null); }
}
