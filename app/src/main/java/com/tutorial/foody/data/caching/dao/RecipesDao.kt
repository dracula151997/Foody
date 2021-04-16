package com.tutorial.foody.data.caching.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tutorial.foody.data.caching.entity.RecipeEntity
import com.tutorial.foody.models.FoodRecipeResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: FoodRecipeResponse)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    suspend fun getCachingRecipes() : Flow<List<RecipeEntity>>


}