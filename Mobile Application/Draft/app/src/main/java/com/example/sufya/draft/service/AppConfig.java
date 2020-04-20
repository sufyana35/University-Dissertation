package com.example.sufya.draft.service;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by sufyan Ahmed on 08/02/2017.
 */

/**
 *Model for retrofit
 */
public class AppConfig {

    public interface insert {
        @FormUrlEncoded
        @POST("/insertExpense.php/")
        void insertData(
                @Field("uniqueid") String uniqueid,
                @Field("category") String category,
                @Field("sub_category") String sub_category,
                @Field("description") String description,
                @Field("price") String price,
                @Field("timestamp") String timestamp,
                Callback<Response> callback);
    }

    public interface deleteRequest {
        @FormUrlEncoded
        @POST("/deleteExpenses.php/")
        void insertData(
                @Field("uniqueid") String uniqueid,
                Callback<Response> callback);
    }

}
