package com.tutorial.foody.data

import com.tutorial.foody.data.network.FoodRecipesApi
import com.tutorial.foody.models.FoodRecipeResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getFoodRecipes(queries: Map<String, String>): Response<FoodRecipeResponse> =
        foodRecipesApi.getRecipes(queries)

    suspend fun searchFoodRecipe(queries: Map<String, String>): Response<FoodRecipeResponse> =
        foodRecipesApi.searchRecipe(queries)
}