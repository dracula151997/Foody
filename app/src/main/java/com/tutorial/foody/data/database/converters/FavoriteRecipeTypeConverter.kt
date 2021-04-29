package com.tutorial.foody.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tutorial.foody.models.RecipeResult

class FavoriteRecipeTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun convertRecipeIntoGson(result: RecipeResult): String = gson.toJson(result)

    @TypeConverter
    fun convertGsonIntoRecipe(result: String): RecipeResult =
        gson.fromJson(result, RecipeResult::class.java)
}