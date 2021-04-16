package com.tutorial.foody.data.caching

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tutorial.foody.data.caching.converter.RecipesTypeConverter
import com.tutorial.foody.data.caching.dao.RecipesDao
import com.tutorial.foody.data.caching.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipesDao

}