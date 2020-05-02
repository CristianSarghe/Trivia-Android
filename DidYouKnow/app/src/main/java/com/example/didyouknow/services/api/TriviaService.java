package com.example.didyouknow.services.api;

import com.example.didyouknow.models.CategoryModel;
import com.example.didyouknow.models.InformationModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TriviaService {
    @GET("/api/trivia/{categoryId}")
    Call<InformationModel> getInformation(@Path("categoryId") long categoryId);

    @GET("/api/trivia/categories")
    Call<CategoryModel[]> getCategories();

    @POST("/api/trivia")
    Call<InformationModel> postUserInformation(@Body() InformationModel model);
}
