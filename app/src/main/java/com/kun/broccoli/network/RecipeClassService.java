package com.kun.broccoli.network;

import com.kun.broccoli.model.recipeClass.RecipeClassResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeClassService {
    @GET("recipe_class?appkey=ac3df7dc8589d4a2fac3ece1990f7c14")
    Call<RecipeClassResponse> getRecipeClass();
}
