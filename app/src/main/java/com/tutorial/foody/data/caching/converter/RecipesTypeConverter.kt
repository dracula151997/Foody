package com.tutorial.foody.data.caching.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tutorial.foody.models.FoodRecipeResponse

class RecipesTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun convertRecipesToJson(foodRecipe: FoodRecipeResponse): String = gson.toJson(foodRecipe)

    @TypeConverter
    fun convertJsonToRecipes(foodRecipe: String): FoodRecipeResponse {
        val type = object : TypeToken<FoodRecipeResponse>() {}.type
        return gson.fromJson(foodRecipe, type)
    }
}