package com.fitlife.conexionServer;

import com.fitlife.model.AgregarProgresoRequest;
import com.fitlife.model.LoginRequest;
import com.fitlife.model.LoginResponse;
import com.fitlife.model.RegisterRequest;
import com.fitlife.model.RegisterResponse;
import com.fitlife.model.ProfileResponse;
import com.fitlife.model.EditProfileRequest;
import com.fitlife.model.GenericResponse;
import com.fitlife.model.ErrorResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;


public interface FitLifeService {
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest req);
    // …añade aquí register(), getRutinas(), etc

    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest req);

    @GET("api/perfil")         // <<< aqui
    Call<ProfileResponse> getProfile();

    @POST("api/editarPerfil")  // <<< y aqui
    Call<GenericResponse> editProfile(@Body EditProfileRequest req);

    @POST("api/agregarProgreso")
    Call<GenericResponse> agregarProgreso(@Body AgregarProgresoRequest request);
}