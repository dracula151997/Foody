package com.tutorial.foody

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tutorial.foody.data.FoodyRepository
import com.tutorial.foody.data.network.NetworkResult
import com.tutorial.foody.models.FoodRecipeResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: FoodyRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _recipeResponse: MutableLiveData<NetworkResult<FoodRecipeResponse>> =
        MutableLiveData()
    val recipeResponse: LiveData<NetworkResult<FoodRecipeResponse>> = _recipeResponse

    fun getFoodRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getFoodRecipesSafeCall(queries)
    }

    private suspend fun getFoodRecipesSafeCall(queries: Map<String, String>) {
        _recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteDataSource.getFoodRecipes(queries)
                _recipeResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                _recipeResponse.value = NetworkResult.Error("Recipes not found")
            }

        } else {
            _recipeResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipeResponse>): NetworkResult<FoodRecipeResponse>? {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("API Key limited.")
            response.body()!!.recipeResults.isNullOrEmpty() -> NetworkResult.Error("Recipes not found.")
            response.isSuccessful -> NetworkResult.Success(response.body()!!)
            else -> NetworkResult.Error(response.message())

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }
}