package com.example.sufya.draft.service;

/**
 * Created by sufyan Ahmed on 23/02/2017.
 */


import com.example.sufya.draft.service.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *attach retrofit services
 */
public class RestClient {

    private static Service service;

    public static Service getClient() {
        if (service == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            // Add logging into retrofit 2.0
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.interceptors().add(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.moblet.club/moblet/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build()).build();

            service = retrofit.create(Service.class);
        }
        return service;
    }
}
