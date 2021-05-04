package com.tutorial.foody

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.tutorial.foody.data.FoodyRepository
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import com.tutorial.foody.data.database.entities.RecipeEntity
import com.tutorial.foody.data.network.NetworkResult
import com.tutorial.foody.models.FoodRecipeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: FoodyRepository,
    application: Application
) : AndroidViewModel(application) {

    /** DATABASE**/
    val cachedRecipes: LiveData<List<RecipeEntity>> =
        repository.localDataSource.getCachedRecipes().asLiveData()

    private fun insertRecipes(recipe: RecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) { repository.localDataSource.insertRecipe(recipe) }

    val favoriteRecipes: LiveData<List<FavoriteRecipeEntity>> =
        repository.localDataSource.getFavoriteRecipes().asLiveData()

    fun checkFavoriteRecipeExist(rowId: Int): LiveData<Boolean> =
        repository.localDataSource.checkFavoriteRecipeExist(rowId).asLiveData()

    fun insertFavoriteRecipe(recipe: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertFavoriteRecipe(
                recipe
            )
        }

    private fun deleteFavoriteRecipeByID(favoriteRecipeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteFavoriteRecipeByID(
                favoriteRecipeId
            )
        }

    fun deleteFavoriteRecipe(recipeEntity: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteFavoriteRecipe(recipeEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) { repository.localDataSource.deleteAllFavoriteRecipes() }


    /**RETROFIT**/
    private val _recipeResponse: MutableLiveData<NetworkResult<FoodRecipeResponse>?> =
        MutableLiveData()
    val recipeResponse: LiveData<NetworkResult<FoodRecipeResponse>?> = _recipeResponse
    private val _searchRecipeResponse: MutableLiveData<NetworkResult<FoodRecipeResponse>> =
        MutableLiveData()
    val searchRecipeResponse: LiveData<NetworkResult<FoodRecipeResponse>> = _searchRecipeResponse

    fun getFoodRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getFoodRecipesSafeCall(queries)
    }

    fun searchFoodRecipe(queries: Map<String, String>) =
        viewModelScope.launch { searchFoodRecipeSafeCall(queries) }

    private suspend fun searchFoodRecipeSafeCall(queries: Map<String, String>) {
        _searchRecipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteDataSource.searchFoodRecipe(queries)
                _searchRecipeResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                _searchRecipeResponse.value = NetworkResult.Error("Recipes not found")
            }

        } else {
            _searchRecipeResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private suspend fun getFoodRecipesSafeCall(queries: Map<String, String>) {
        _recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteDataSource.getFoodRecipes(queries)
                _recipeResponse.value = handleFoodRecipesResponse(response)

                val recipeResponseFromLiveData = _recipeResponse.value!!.data
                if (recipeResponseFromLiveData != null) {
                    offlineCacheRecipes(recipeResponseFromLiveData)
                }
            } catch (e: Exception) {
                _recipeResponse.value = NetworkResult.Error("Recipes not found")
            }

        } else {
            _recipeResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun offlineCacheRecipes(recipe: FoodRecipeResponse) {
        val recipeEntity = RecipeEntity(recipe)
        insertRecipes(recipeEntity)
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