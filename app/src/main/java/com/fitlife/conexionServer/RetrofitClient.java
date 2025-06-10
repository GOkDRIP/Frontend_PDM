package com.fitlife.conexionServer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/fitlife-1.0-SNAPSHOT/";
            //"http://192.168.18.34:8080/fitlife-1.0-SNAPSHOT/";
    private static Retrofit retrofit = null;

    public static synchronized FitLifeService getService() {
        if (retrofit == null) {
            // 1) Logging interceptor (opcional pero útil)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // 2) Construir retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(FitLifeService.class);
    }
}