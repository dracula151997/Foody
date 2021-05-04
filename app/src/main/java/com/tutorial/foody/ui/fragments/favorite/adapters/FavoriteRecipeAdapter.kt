package com.tutorial.foody.ui.fragments.favorite.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.foody.R
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import com.tutorial.foody.databinding.FavoriteRecipeRowLayoutBinding
import com.tutorial.foody.ui.DiffUtils
import com.tutorial.foody.ui.fragments.favorite.FavoriteRecipesFragmentDirections

class FavoriteRecipeAdapter(
    private val requireActivity: FragmentActivity
) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.FavoriteRecipeViewHolder>(), ActionMode.Callback {

    private var _isMultiSelecttion = false
    private var selectedRecipes = arrayListOf<FavoriteRecipeEntity>()
    private val favoritRecipeViewHolders = arrayListOf<FavoriteRecipeViewHolder>()

    private var favoriteRecipes = emptyList<FavoriteRecipeEntity>()

    fun setFavoriteRecipes(newFavoriteRecipes: List<FavoriteRecipeEntity>) {
        val favoriteRecipesDiffUtil = DiffUtils(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipeViewHolder =
        FavoriteRecipeViewHolder.from(parent)

    override fun onBindViewHolder(holder: FavoriteRecipeViewHolder, position: Int) {
        val favoriteRecipe = favoriteRecipes[position]
        favoritRecipeViewHolders.add(holder)
        holder.bind(favoriteRecipe)
        /**
         * Long click listener -> for enabling Action Mode
         */
        holder.itemView.setOnLongClickListener {
            if (!_isMultiSelecttion) {
                _isMultiSelecttion = true
                requireActivity.startActionMode(this)
                selectRecipeAction(holder, favoriteRecipe)
                true
            } else {
                _isMultiSelecttion = false
                false
            }
        }

        /**
         * Single Click Listener
         */
        holder.itemView.setOnClickListener {
            if (_isMultiSelecttion) {
                selectRecipeAction(holder, favoriteRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToRecipeDetailsActivity(
                        favoriteRecipe.recipeResult
                    )
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = favoriteRecipes.size

    class FavoriteRecipeViewHolder(
        val binding: FavoriteRecipeRowLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteRecipe: FavoriteRecipeEntity) {
            binding.favoriteRecipe = favoriteRecipe
            binding.executePendingBindings()
        }

        companion object {
            fun from(viewGroup: ViewGroup): FavoriteRecipeViewHolder {
                val inflater = LayoutInflater.from(viewGroup.context)
                val binding = FavoriteRecipeRowLayoutBinding.inflate(inflater, viewGroup, false)
                return FavoriteRecipeViewHolder(binding)
            }
        }

    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        changeStatusBarActionModeColor(R.color.contextual_status_bar_color)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        changeCardsColor()
        changeStatusBarActionModeColor(R.color.primary_status_bar_color)
        _isMultiSelecttion = false
        selectedRecipes.clear()
    }

    private fun changeCardsColor() = favoritRecipeViewHolders.forEach {
        changeRecipeRowLayoutStyle(
            it.binding,
            android.R.color.white,
            R.color.recipeCardStrokeColor
        )
    }

    private fun changeStatusBarActionModeColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(
            requireActivity,
            color
        )
    }

    private fun selectRecipeAction(holder: FavoriteRecipeViewHolder, recipe: FavoriteRecipeEntity) {
        if (selectedRecipes.contains(recipe)) {
            selectedRecipes.remove(recipe)
            changeRecipeRowLayoutStyle(
                holder.binding,
                android.R.color.background_light,
                R.color.recipeCardStrokeColor
            )
        } else {
            selectedRecipes.add(recipe)
            changeRecipeRowLayoutStyle(
                holder.binding,
                android.R.color.background_light,
                R.color.primary_status_bar_color
            )
        }

    }

    private fun changeRecipeRowLayoutStyle(
        binding: FavoriteRecipeRowLayoutBinding,
        backgoundColor: Int,
        strokeColor: Int
    ) {
        binding.favoriteRecipeRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgoundColor
            )
        )

        binding.recipeRowCard.strokeColor = ContextCompat.getColor(
            requireActivity,
            strokeColor
        )
    }
}