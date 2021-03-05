package com.gcr.retrofit_sqlite.api;

import com.gcr.retrofit_sqlite.models.FlowerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("feeds/flowers.json")
    Call<List<FlowerResponse>> getDataFromServer();
}
