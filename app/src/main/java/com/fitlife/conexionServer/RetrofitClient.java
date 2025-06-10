package com.fitlife.conexionServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.CookieManager;
import java.net.CookiePolicy;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/fitlife-1.0-SNAPSHOT/";
    private static Retrofit retrofit = null;

    public static synchronized FitLifeService getService() {
        if (retrofit == null) {
            // 1) Cookie manager que acepta todas las cookies
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            // 2) Logging interceptor (opcional pero útil)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 3) Cliente OkHttp con CookieJar y logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .addInterceptor(logging)
                    .build();

            // 4) Gson lenient por si hay ligeras imperfecciones en el JSO n
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // 5) Construir Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(FitLifeService.class);
    }
}
