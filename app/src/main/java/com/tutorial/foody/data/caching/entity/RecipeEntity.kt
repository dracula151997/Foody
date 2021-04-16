package com.tutorial.foody.data.caching.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tutorial.foody.models.FoodRecipeResponse

@Entity(tableName = "recipes_table")
class RecipeEntity(
    var foodRecipe: FoodRecipeResponse
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}