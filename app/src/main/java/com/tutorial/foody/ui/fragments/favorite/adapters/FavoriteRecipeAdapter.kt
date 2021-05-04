package com.tutorial.foody.ui.fragments.favorite.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tutorial.foody.MainViewModel
import com.tutorial.foody.R
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import com.tutorial.foody.databinding.FavoriteRecipeRowLayoutBinding
import com.tutorial.foody.ui.DiffUtils
import com.tutorial.foody.ui.fragments.favorite.FavoriteRecipesFragmentDirections

class FavoriteRecipeAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.FavoriteRecipeViewHolder>(), ActionMode.Callback {

    private var _isMultiSelection = false
    private lateinit var _actionMode: ActionMode
    private lateinit var _rootView: View
    private var selectedRecipes = arrayListOf<FavoriteRecipeEntity>()
    private val favoriteRecipeViewHolders = arrayListOf<FavoriteRecipeViewHolder>()

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
        _rootView = holder.itemView.rootView
        val favoriteRecipe = favoriteRecipes[position]
        favoriteRecipeViewHolders.add(holder)
        holder.bind(favoriteRecipe)
        /**
         * Long click listener -> for enabling Action Mode
         */
        holder.itemView.setOnLongClickListener {
            if (!_isMultiSelection) {
                _isMultiSelection = true
                requireActivity.startActionMode(this)
                applyRecipeSelectionAction(holder, favoriteRecipe)
                true
            } else {
                _isMultiSelection = false
                false
            }
        }

        /**
         * Single Click Listener
         */
        holder.itemView.setOnClickListener {
            if (_isMultiSelection) {
                applyRecipeSelectionAction(holder, favoriteRecipe)
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
        _actionMode = mode!!
        changeStatusBarActionModeColor(R.color.contextual_status_bar_color)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            selectedRecipes.forEach { mainViewModel.deleteFavoriteRecipe(it) }
        }
        showSnackBar("${selectedRecipes.size} recipe(s) removed.")
        _isMultiSelection = false
        selectedRecipes.clear()
        mode?.finish()
        return true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            _rootView,
            message,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.okay) {}.show()
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        changeCardsColor()
        changeStatusBarActionModeColor(R.color.primary_status_bar_color)
        _isMultiSelection = false
        selectedRecipes.clear()
    }

    private fun changeCardsColor() = favoriteRecipeViewHolders.forEach {
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

    private fun applyRecipeSelectionAction(
        holder: FavoriteRecipeViewHolder,
        recipe: FavoriteRecipeEntity
    ) {
        if (selectedRecipes.contains(recipe)) {
            selectedRecipes.remove(recipe)
            changeRecipeRowLayoutStyle(
                holder.binding,
                android.R.color.background_light,
                R.color.recipeCardStrokeColor
            )
            setActionModeTitle()
        } else {
            selectedRecipes.add(recipe)
            changeRecipeRowLayoutStyle(
                holder.binding,
                android.R.color.background_light,
                R.color.primary_status_bar_color
            )
            setActionModeTitle()
        }

    }

    private fun changeRecipeRowLayoutStyle(
        binding: FavoriteRecipeRowLayoutBinding,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        binding.favoriteRecipeRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )

        binding.recipeRowCard.strokeColor = ContextCompat.getColor(
            requireActivity,
            strokeColor
        )
    }

    private fun setActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> _actionMode.finish()
            1 -> _actionMode.title = "${selectedRecipes.size} item selected"
            else -> _actionMode.title = "${selectedRecipes.size} items selected"
        }
    }
}