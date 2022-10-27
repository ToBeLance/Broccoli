package com.kun.broccoli.network;

import com.kun.broccoli.model.cookbook.SingleCookBookSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeSearchByDetail {
    @GET("detail?appkey=ac3df7dc8589d4a2fac3ece1990f7c14")
    Call<SingleCookBookSearchResponse> getRecipeByDetail(@Query("id") String id);
}
