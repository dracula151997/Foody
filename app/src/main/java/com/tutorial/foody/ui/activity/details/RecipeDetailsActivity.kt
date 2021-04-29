package com.tutorial.foody.ui.activity.details

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.tutorial.foody.MainViewModel
import com.tutorial.foody.R
import com.tutorial.foody.data.database.entities.FavoriteRecipeEntity
import com.tutorial.foody.databinding.ActivityRecipeDetailsBinding
import com.tutorial.foody.ui.activity.details.adapter.PagerAdapter
import com.tutorial.foody.ui.fragments.ingredient.IngredientFragment
import com.tutorial.foody.ui.fragments.instruction.InstructionFragment
import com.tutorial.foody.ui.fragments.overview.OverviewFragment
import com.tutorial.foody.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsActivity : AppCompatActivity() {
    companion object {
        const val TAG = "RecipeDetailsActivity"
    }

    private val args by navArgs<RecipeDetailsActivityArgs>()
    private var _binding: ActivityRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupViewPagerWithTabLayout()
    }

    private fun setupViewPagerWithTabLayout() {
        val resultBundle = Bundle()
        resultBundle.putParcelable(Constants.RECIPE_RESULT, args.result)

        val fragments =
            arrayListOf(OverviewFragment(), IngredientFragment(), InstructionFragment())

        val titles = arrayListOf(
            getString(R.string.overview),
            getString(R.string.ingredient),
            getString(R.string.instructions)
        )
        val pagerAdapter = PagerAdapter(
            result = resultBundle,
            fragments = fragments,
            titles = titles,
            supportFragmentManager,
        )

        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_details_menu, menu)
        val favoriteMenuItem = menu?.findItem(R.id.menu_favorite)
        checkFavoriteRecipeExist(favoriteMenuItem)
        return true
    }

    private fun checkFavoriteRecipeExist(menuItem: MenuItem?) {
        mainViewModel.favoriteRecipes.observe(this, { favoriteRecipes ->
            try {
                for (favoriteRecipe in favoriteRecipes) {
                    if (favoriteRecipe.recipeResult.id == args.result.id) {
                        changeFavoriteMenuIcon(menuItem!!, R.color.red_500)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "checkFavoriteRecipeExist: exception ${e.message}")
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.menu_favorite) {
            saveRecipeIntoFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveRecipeIntoFavorite(item: MenuItem) {
        val favoriteRecipeEntity = FavoriteRecipeEntity(
            0,
            args.result
        )

        mainViewModel.insertFavoriteRecipe(favoriteRecipeEntity)
        changeFavoriteMenuIcon(item, R.color.red_500)
        showSnackbar(R.string.recipe_saved, R.string.okay)

    }

    private fun showSnackbar(message: Int, actionStringId: Int) = Snackbar.make(
        binding.recipeDetailsContainer,
        message,
        Snackbar.LENGTH_LONG
    ).setAction(actionStringId) {}
        .show()

    private fun changeFavoriteMenuIcon(item: MenuItem, color: Int) =
        item.icon.setTint(ContextCompat.getColor(this, color))


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}