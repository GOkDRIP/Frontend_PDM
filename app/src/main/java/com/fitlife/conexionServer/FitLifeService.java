package com.fitlife.conexionServer;

import com.fitlife.model.AgregarProgresoRequest;
import com.fitlife.model.AnalisisResponse;
import com.fitlife.model.AsignarRutinaResponse;
import com.fitlife.model.DesafiosResponse;
import com.fitlife.model.EditProfileRequest;
import com.fitlife.model.GenericResponse;
import com.fitlife.model.ListarComidasResponse;
import com.fitlife.model.ListarProgresosResponse;
import com.fitlife.model.LoginRequest;
import com.fitlife.model.LoginResponse;
import com.fitlife.model.MisDesafiosResponse;
import com.fitlife.model.ProfileResponse;
import com.fitlife.model.RecuperarContrasenaRequest;
import com.fitlife.model.RecuperarContrasenaResponse;
import com.fitlife.model.RegisterRequest;
import com.fitlife.model.RegisterResponse;
import com.fitlife.model.RegistrarComidaRequest;
import com.fitlife.model.RestablecerContrasenaRequest;
import com.fitlife.model.RestablecerContrasenaResponse;
import com.fitlife.model.Rutina;
import com.fitlife.model.RutinaRequest;
import com.fitlife.model.VerRutinaActualResponse;
import com.fitlife.model.ObjetivosDiaResponse;
import com.fitlife.model.ComidaFeedResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.DELETE;


public interface FitLifeService {

    // ---------- AUTENTICACIÓN & PERFIL ----------
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest req);

    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest req);

    @GET("api/perfil")
    Call<ProfileResponse> getProfile();

    @POST("api/editarPerfil")
    Call<GenericResponse> editProfile(@Body EditProfileRequest req);

    // ---------- PROGRESO & RUTINAS ----------
    @POST("api/agregarProgreso")
    Call<GenericResponse> agregarProgreso(@Body AgregarProgresoRequest req);

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

    // ---------- DESAFÍOS ----------
    @GET("api/desafios")
    Call<DesafiosResponse> obtenerDesafios();

    @POST("api/unirse")
    Call<GenericResponse> unirseDesafio(@Body Map<String, Integer> body);

    @GET("api/mis-desafios")
    Call<MisDesafiosResponse> getMisDesafios();

    @FormUrlEncoded
    @POST("api/abandonar")
    Call<GenericResponse> abandonarDesafio(@Field("id") int desafioId);

    @FormUrlEncoded
    @POST("api/completar")
    Call<GenericResponse> completarDesafio(@Field("id") int desafioId);

    // ---------- CONTRASEÑA ----------
    @POST("api/recuperar")
    Call<RecuperarContrasenaResponse> recuperarContrasena(@Body RecuperarContrasenaRequest request);

    @POST("api/restablecer")
    Call<RestablecerContrasenaResponse> restablecerContrasena(@Body RestablecerContrasenaRequest request);

    // ---------- COMIDAS PUBLICADAS (NUEVA RED SOCIAL) ----------
    @GET("api/listarComidas")
    Call<ListarComidasResponse> listarComidas();

    @POST("api/registrarComida")
    Call<GenericResponse> registrarComida(@Body RegistrarComidaRequest req);

    @Multipart
    @POST("api/comidas")
    Call<GenericResponse> publicarComida(
            @Part MultipartBody.Part imagen,
            @Part("nombre") RequestBody nombre,
            @Part("calorias") RequestBody calorias,
            @Part("carbohidratos") RequestBody carbohidratos,
            @Part("proteinas") RequestBody proteinas,
            @Part("grasas") RequestBody grasas,
            @Part("descripcion") RequestBody descripcion
    );

    @GET("api/feed")
    Call<ComidaFeedResponse> getFeed();

    @FormUrlEncoded
    @POST("api/like")
    Call<GenericResponse> like(@Field("id") int idPublicacion);

    @FormUrlEncoded
    @POST("api/unlike")
    Call<GenericResponse> unlike(@Field("id") int idPublicacion);

    @FormUrlEncoded
    @POST("api/comidas/eliminar")
    Call<GenericResponse> eliminarComida(@Field("id") int idPublicacion);

    @POST("api/analyze")
    Call<AnalisisResponse> analizaEtiqueta(@Body Map<String,String> body);

    @DELETE("api/eliminar-usuario")
    Call<GenericResponse> eliminarUsuario();
}
