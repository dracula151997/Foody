package com.tutorial.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tutorial.foody.data.database.converters.RecipesTypeConverter
import com.tutorial.foody.data.database.dao.FavoriteRecipeDao
import com.tutorial.foody.data.database.dao.RecipesDao
import com.tutorial.foody.data.database.entities.RecipeEntity
import com.tutorial.foody.ui.fragments.favorite.FavoriteRecipesFragment

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class, FavoriteRecipesFragment::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipesDao
    abstract fun favoriteRecipeDao() : FavoriteRecipeDao

}