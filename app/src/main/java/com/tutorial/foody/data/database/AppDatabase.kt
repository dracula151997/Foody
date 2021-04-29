package com.tutorial.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tutorial.foody.data.database.converters.FavoriteRecipeTypeConverter
import com.tutorial.foody.data.database.converters.RecipesTypeConverter
import com.tutorial.foody.data.database.dao.FavoriteRecipeDao
import com.tutorial.foody.data.database.dao.RecipesDao
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import com.tutorial.foody.data.database.entities.RecipeEntity
import com.tutorial.foody.ui.fragments.favorite.FavoriteRecipesFragment

@Database(
    entities = [RecipeEntity::class, FavoriteRecipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class, FavoriteRecipeTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipesDao
    abstract fun favoriteRecipeDao() : FavoriteRecipeDao

}