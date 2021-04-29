package com.tutorial.foody.bindingadapters

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import com.tutorial.foody.ui.fragments.favorite.adapters.FavoriteRecipeAdapter

class FavoriteRecipesFragmentBinding {
    companion object {
        @BindingAdapter("setFavoriteRecipeAdapter")
        @JvmStatic
        fun setFavoriteRecipesAdapter(recyclerView: RecyclerView, adapter: FavoriteRecipeAdapter) {
            recyclerView.adapter = adapter
        }

        @BindingAdapter("viewVisibilty", "favoriteRecipesAdapter", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoriteEntity: List<FavoriteRecipeEntity>?,
            adapter: FavoriteRecipeAdapter?
        ) {
            if (favoriteEntity.isNullOrEmpty()) {
                when (view) {
                    is LinearLayout -> view.isVisible = true
                    is RecyclerView -> view.isVisible = false
                }
            } else {
                when (view) {
                    is LinearLayout -> view.isVisible = false
                    is RecyclerView -> {
                        view.isVisible = true
                        adapter?.setFavoriteRecipes(favoriteEntity)
                        view.adapter = adapter
                    }

                }
            }
        }
    }
}