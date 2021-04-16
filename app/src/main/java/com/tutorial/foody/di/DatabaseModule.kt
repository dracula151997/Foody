package com.tutorial.foody.di

import android.content.Context
import androidx.room.Room
import com.tutorial.foody.data.caching.AppDatabase
import com.tutorial.foody.data.caching.dao.RecipesDao
import com.tutorial.foody.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideRecipesDao(
        appDatabase: AppDatabase
    ): RecipesDao = appDatabase.recipeDao()
}