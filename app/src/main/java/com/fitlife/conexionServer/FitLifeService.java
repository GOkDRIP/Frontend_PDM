package com.fitlife.conexionServer;

import com.fitlife.model.AgregarProgresoRequest;
import com.fitlife.model.LoginRequest;
import com.fitlife.model.LoginResponse;
import com.fitlife.model.RegisterRequest;
import com.fitlife.model.RegisterResponse;
import com.fitlife.model.ProfileResponse;
import com.fitlife.model.EditProfileRequest;
import com.fitlife.model.GenericResponse;
import com.fitlife.model.ListarComidasResponse;
import com.fitlife.model.RegistrarComidaRequest;
import com.fitlife.model.ListarProgresosResponse;
import com.fitlife.model.AsignarRutinaResponse;
import com.fitlife.model.Rutina;
import com.fitlife.model.RutinaRequest;
import com.fitlife.model.VerRutinaActualResponse;
import com.fitlife.model.ObjetivosDiaResponse;
import com.fitlife.model.ErrorResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Header;

import java.util.Map;
import java.util.List;

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
    Call<GenericResponse> agregarProgreso(@Body AgregarProgresoRequest req);

    @GET("api/listarComidas")
    Call<ListarComidasResponse> listarComidas();


    @POST("api/registrarComida")
    Call<GenericResponse> registrarComida(@Body RegistrarComidaRequest req);

    @GET("api/listarProgresos")
    Call<ListarProgresosResponse> listarProgresos(
            @Query("desde") String desde,
            @Query("hasta") String hasta
    );

    @POST("api/listarProgresos")
    Call<ListarProgresosResponse> listarProgresosPost(
            @Body Map<String, String> filtros
    );
    @GET("api/verRutinaActual")
    Call<VerRutinaActualResponse> getVerRutinaActual();

    @GET("api/rutinas")
    Call<List<Rutina>> getTodasLasRutinas();

    @POST("api/asignarRutina")
    Call<AsignarRutinaResponse> postAsignarRutina(@Body RutinaRequest request);

    @GET("api/objetivosDia")
    Call<ObjetivosDiaResponse> getObjetivosDia();



}