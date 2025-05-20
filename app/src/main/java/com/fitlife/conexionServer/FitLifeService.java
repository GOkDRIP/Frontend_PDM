package com.fitlife.conexionServer;

import com.fitlife.model.LoginRequest;
import com.fitlife.model.LoginResponse;
import com.fitlife.model.RegisterRequest;
import com.fitlife.model.RegisterResponse;
import com.fitlife.model.ErrorResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FitLifeService {
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest req);
    // …añade aquí register(), getRutinas(), etc.

    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest req);


}