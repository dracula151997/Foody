package com.tutorial.foody.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.tutorial.foody.utils.Constants
import com.tutorial.foody.utils.Constants.Companion.DATA_STORE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedMealType = preferencesKey<String>(Constants.PREF_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(Constants.PREF_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(Constants.PREF_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(Constants.PREF_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(Constants.PREF_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(DATA_STORE_NAME)

    suspend fun saveMealAndDietType(
        selectedDietType: String,
        selectedDietTypeId: Int,
        selectedMealType: String,
        selectedMealTypeId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedDietType] = selectedDietType
            preferences[PreferenceKeys.selectedDietTypeId] = selectedDietTypeId
            preferences[PreferenceKeys.selectedMealType] = selectedMealType
            preferences[PreferenceKeys.selectedMealTypeId] = selectedMealTypeId

        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.backOnline] = backOnline

        }
    }

    val readBackOnlinePreference: Flow<Boolean> = dataStore.data.catch { exception ->
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception
    }.map { preference ->
        val backOnline = preference[PreferenceKeys.backOnline] ?: false
        backOnline

    }


    val readMealAndDietTypePreferences: Flow<MealAndDietType> = dataStore.data.catch { exception ->
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception

    }.map { preferences ->
        val selectedMealType =
            preferences[PreferenceKeys.selectedMealType] ?: Constants.DEFAULT_MEAL_TYPE
        val selectedDietType =
            preferences[PreferenceKeys.selectedDietType] ?: Constants.DEFAULT_DIET_TYPE
        val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
        val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
        MealAndDietType(selectedMealType, selectedMealTypeId, selectedDietType, selectedDietTypeId)
    }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)