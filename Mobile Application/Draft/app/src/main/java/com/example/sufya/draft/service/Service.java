package com.example.sufya.draft.service;

import com.example.sufya.draft.models.Expense;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sufyan Ahmed on 23/02/2017.
 */

public interface Service {
    @GET("getExpenses.php")
    Call<List<Expense>> loadSizes(@Query("uniqueid") String uniqueid);
}