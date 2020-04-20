package com.example.sufya.draft.service;

/**
 * @Author Ajmal, R. (2016).
 * @Website https://www.learn2crack.com/2016/04/android-login-registration-php-mysql-client.html
 *
 */

import com.example.sufya.draft.models.ServerRequest;
import com.example.sufya.draft.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("moblet-add/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}