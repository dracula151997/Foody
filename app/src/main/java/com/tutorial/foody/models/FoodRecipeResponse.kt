package com.tutorial.foody.models

import com.google.gson.annotations.SerializedName

data class FoodRecipeResponse(
    @SerializedName("results")
    val recipeResults: List<RecipeResult>
)