package com.tutorial.foody.data

import com.tutorial.foody.data.database.dao.FavoriteRecipeDao
import com.tutorial.foody.data.database.dao.RecipesDao
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import com.tutorial.foody.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao,
    private val favoriteRecipeDao: FavoriteRecipeDao
) {

    suspend fun insertRecipe(recipeEntity: RecipeEntity) = recipesDao.insertRecipe(recipeEntity)
    fun getCachedRecipes(): Flow<List<RecipeEntity>> = recipesDao.getCachedRecipes()
    suspend fun insertFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity) =
        favoriteRecipeDao.insertFavoriteRecipe(favoriteRecipeEntity)

    fun getFavoriteRecipes(): Flow<List<FavoriteRecipeEntity>> =
        favoriteRecipeDao.getFavoriteRecipes()

    suspend fun deleteFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity) =
        favoriteRecipeDao.deleteFavoriteRecipe(favoriteRecipeEntity)

    suspend fun deleteFavoriteRecipeByID(entityId: Int) =
        favoriteRecipeDao.deleteFavoriteRecipeByID(entityId)

    suspend fun deleteAllFavoriteRecipes() = favoriteRecipeDao.deleteAllFavoriteRecipes()
}