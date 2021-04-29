package com.tutorial.foody.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tutorial.foody.models.RecipeResult

@Entity(tableName = "favorites_table")
class FavoriteRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "recipe")
    val recipeResult: RecipeResult
) {
}