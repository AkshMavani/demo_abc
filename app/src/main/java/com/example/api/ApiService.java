package com.example.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("objects")
    Call<apimodel> createObject(@Body apimodel postData);

    @PUT("objects/{id}")
    Call<apimodel> updateObject(
            @Path("id") String id,
            @Body apimodel apiModel
    );
}
