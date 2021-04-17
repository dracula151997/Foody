package com.tutorial.foody.bindingadapters

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.tutorial.foody.data.caching.entity.RecipeEntity
import com.tutorial.foody.data.network.NetworkResult
import com.tutorial.foody.models.FoodRecipeResponse

class RecipesFragmentBinding {
    companion object {
        @BindingAdapter("foodRecipesResponse", "cachedRecipes", requireAll = true)
        @JvmStatic
        fun setErrorLayoutVisibility(
            view: View,
            apiResponse: NetworkResult<FoodRecipeResponse>?,
            database: List<RecipeEntity>?
        ) {
            view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
        }
    }
}