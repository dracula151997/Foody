package com.tutorial.foody.data

import com.tutorial.foody.data.database.dao.RecipesDao
import com.tutorial.foody.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    suspend fun insertRecipe(recipeEntity: RecipeEntity) = recipesDao.insertRecipe(recipeEntity)
    fun getCachedRecipes(): Flow<List<RecipeEntity>> = recipesDao.getCachedRecipes()
}