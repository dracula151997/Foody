package com.tutorial.foody.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertFavoriteRecipe(recipe: FavoriteRecipeEntity)

    @Query("SELECT * FROM favorites_table ORDER BY id ASC")
    fun getFavoriteRecipes(): Flow<List<FavoriteRecipeEntity>>

    @Query("DELETE FROM favorites_table")
    suspend fun deleteAllFavoriteRecipes()

    @Delete
    suspend fun deleteFavoriteRecipe(recipe: FavoriteRecipeEntity)

    @Query("DELETE FROM favorites_table WHERE id= :favoriteId")
    suspend fun deleteFavoriteRecipeByID(favoriteId: Int)

    @Query("SELECT EXISTS(SELECT * FROM favorites_table WHERE id= :id)")
    fun isFavoriteRecipeExist(id: Int) : Flow<Boolean>

}