package com.tutorial.foody.network

import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries : Map<String, String>,
    ) //TODO return FoodRecipeResponse

}