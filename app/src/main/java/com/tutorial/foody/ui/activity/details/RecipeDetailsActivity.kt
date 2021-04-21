package com.tutorial.foody.ui.activity.details

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import com.tutorial.foody.R
import com.tutorial.foody.databinding.ActivityRecipeDetailsBinding
import com.tutorial.foody.ui.activity.details.adapter.PagerAdapter
import com.tutorial.foody.ui.fragments.instruction.InstructionFragment
import com.tutorial.foody.ui.fragments.overview.OverviewFragment
import com.tutorial.foody.ui.fragments.tabs.IngredientFragment
import com.tutorial.foody.utils.Constants

class RecipeDetailsActivity : AppCompatActivity() {
    private val args by navArgs<RecipeDetailsActivityArgs>()
    private var _binding: ActivityRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
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
}