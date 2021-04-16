package com.tutorial.foody.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tutorial.foody.MainViewModel
import com.tutorial.foody.data.network.NetworkResult
import com.tutorial.foody.databinding.FragmentRecipesBinding
import com.tutorial.foody.ui.fragments.recipes.adapter.RecipesAdapter
import com.tutorial.foody.utils.ApiQuery
import com.tutorial.foody.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentRecipesBinding
    private val recipesAdapter by lazy { RecipesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setupRecyclerView()
        readCachedRecipes()
    }

    private fun readCachedRecipes() = lifecycleScope.launchWhenStarted {
        mainViewModel.cachedRecipes.observe(viewLifecycleOwner, { recipes ->
            if (recipes.isNullOrEmpty()) {
                getRecipesFromAPI()
            } else {
                hideShimmerEffect()
                recipesAdapter.setRecipes(recipes[0].foodRecipe)
            }
        })
    }


    private fun getRecipesFromAPI() {
        mainViewModel.getFoodRecipes(recipeQueries())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Loading -> showShimmerEffect()
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { recipesAdapter.setRecipes(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadRecipesFromCache()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun recipeQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[ApiQuery.QUERY_NUMBER] = "50"
        queries[ApiQuery.QUERY_API_KEY] = Constants.API_KEY
        queries[ApiQuery.QUERY_TYPE] = "snack"
        queries[ApiQuery.QUERY_DIET] = "vegan"
        queries[ApiQuery.QUERY_ADD_RECEIPT_INFO] = "true"
        queries[ApiQuery.QUERY_FILL_INGREDIENTS] = "true"

        return queries


    }

    private fun loadRecipesFromCache() = lifecycleScope.launchWhenStarted {
        mainViewModel.cachedRecipes.observe(viewLifecycleOwner, { recipes ->
            if (recipes.isNotEmpty())
                recipesAdapter.setRecipes(recipes[0].foodRecipe)
        })
    }


    private fun setupRecyclerView() = apply { binding.recipesRecyclerView.adapter = recipesAdapter }

    private fun showShimmerEffect() = binding.recipesRecyclerView.showShimmer()
    private fun hideShimmerEffect() = binding.recipesRecyclerView.hideShimmer()

}