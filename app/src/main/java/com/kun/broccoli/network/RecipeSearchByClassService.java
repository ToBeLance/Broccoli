package com.kun.broccoli.network;

import com.kun.broccoli.model.cookbook.CookBooksSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeSearchByClassService {
    @GET("byclass?start=0&num=10&appkey=ac3df7dc8589d4a2fac3ece1990f7c14")
    Call<CookBooksSearchResponse> getRecipeByClass(@Query("classid") String classid);
}
