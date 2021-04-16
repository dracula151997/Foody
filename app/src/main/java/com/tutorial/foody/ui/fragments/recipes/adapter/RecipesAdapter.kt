package com.tutorial.foody.ui.fragments.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.foody.data.network.FoodRecipesApi
import com.tutorial.foody.databinding.RecipesRowLayoutBinding
import com.tutorial.foody.models.FoodRecipeResponse
import com.tutorial.foody.models.RecipeResult

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<RecipeResult>()

    class RecipeViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeResult) {
            binding.recipe = recipe
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return RecipeViewHolder(binding)
            }
        }

    }

    fun setRecipes(recipe: FoodRecipeResponse){
        recipes = recipe.recipeResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder =
        RecipeViewHolder.from(parent)

    override fun onBindViewHolder(holderRecipe: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holderRecipe.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size
}