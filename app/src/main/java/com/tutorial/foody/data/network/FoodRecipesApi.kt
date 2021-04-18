package com.tutorial.foody.data.network

import com.tutorial.foody.models.FoodRecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>,
    ): Response<FoodRecipeResponse>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipe(
        @QueryMap searchQueries: Map<String, String>,
    ) : Response<FoodRecipeResponse>

}