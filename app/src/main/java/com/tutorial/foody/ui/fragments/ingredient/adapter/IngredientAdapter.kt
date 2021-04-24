package com.tutorial.foody.ui.fragments.ingredient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.foody.databinding.IngredientRowLayoutBinding
import com.tutorial.foody.models.ExtendedIngredient
import com.tutorial.foody.models.RecipeResult
import com.tutorial.foody.ui.DiffUtils

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {
    private var ingredients = emptyList<ExtendedIngredient>()

    fun setIngredients(recipeResult: RecipeResult) {
        val ingredientDiffUtil =
            DiffUtils(ingredients, recipeResult.extendedIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredients = recipeResult.extendedIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder =
        IngredientViewHolder.from(parent)

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) =
        holder.bind(getIngredient(position))

    override fun getItemCount(): Int = ingredients.size

    private fun getIngredient(position: Int) = ingredients[position]

    class IngredientViewHolder(private val binding: IngredientRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: ExtendedIngredient) {
            binding.ingredient = ingredient
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): IngredientViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = IngredientRowLayoutBinding.inflate(inflater, parent, false)
                return IngredientViewHolder(binding)
            }
        }

    }
}