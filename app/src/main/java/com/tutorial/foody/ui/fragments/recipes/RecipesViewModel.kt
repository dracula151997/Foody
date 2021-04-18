package com.tutorial.foody.ui.fragments.recipes

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tutorial.foody.data.DataStoreRepository
import com.tutorial.foody.utils.ApiQuery
import com.tutorial.foody.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) :
    AndroidViewModel(application) {

    private var mealType: String = Constants.DEFAULT_MEAL_TYPE
    private var dietType: String = Constants.DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietTypePreferences
    val readBackOnline = dataStoreRepository.readBackOnlinePreference.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(dietType, dietTypeId, mealType, mealTypeId)
        }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) { dataStoreRepository.saveBackOnline(backOnline) }

    fun applyQueries(): Map<String, String> {

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
        val queries = HashMap<String, String>()
        queries[ApiQuery.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[ApiQuery.QUERY_API_KEY] = Constants.API_KEY
        queries[ApiQuery.QUERY_FILL_INGREDIENTS] = "true"
        queries[ApiQuery.QUERY_ADD_RECEIPT_INFO] = "true"
        queries[ApiQuery.QUERY_DIET] = dietType
        queries[ApiQuery.QUERY_TYPE] = mealType

        return queries
    }

    fun applySearchQueries(searchQuery: String): Map<String, String> {
        val queries = HashMap<String, String>()
        queries[ApiQuery.QUERY_SEARCH] = searchQuery
        queries[ApiQuery.QUERY_API_KEY] = Constants.API_KEY
        queries[ApiQuery.QUERY_FILL_INGREDIENTS] = "true"
        queries[ApiQuery.QUERY_ADD_RECEIPT_INFO] = "true"
        queries[ApiQuery.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)

        } else {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}