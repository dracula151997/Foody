package com.tutorial.foody.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface FavoriteRecipeDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertFavoriteRecipe(recipe: FavoriteRecipeEntity)

    @Query("SELECT * FROM favorites_table ORDER BY id ASC")
    fun getFavoriteRecipes(): Flow<List<FavoriteRecipeEntity>>

    @DELETE
    suspend fun deleteFavoriteRecipe(recipe: FavoriteRecipeEntity)

    @Query("DELETE FROM favorites_table")
    suspend fun deleteAllFavoriteRecipes()

    @Query("DELETE FROM favorites_table WHERE id= :favoriteId")
    suspend fun deleteFavoriteRecipeByID(favoriteId: Int)

}