package com.tutorial.foody.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tutorial.foody.MainViewModel
import com.tutorial.foody.R
import com.tutorial.foody.data.network.NetworkResult
import com.tutorial.foody.databinding.FragmentRecipesBinding
import com.tutorial.foody.ui.fragments.recipes.adapter.RecipesAdapter
import com.tutorial.foody.utils.ApiQuery
import com.tutorial.foody.utils.Constants
import com.tutorial.foody.utils.log
import com.tutorial.foody.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val recipesAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setupRecyclerView()
        readCachedRecipes()

        binding.fab.setOnClickListener { findNavController().navigate(R.id.action_recipesFragment_to_recipesFilterBottomSheet) }
    }

    private fun readCachedRecipes() = lifecycleScope.launchWhenStarted {
        mainViewModel.cachedRecipes.observeOnce(viewLifecycleOwner, { recipes ->
            if (recipes.isNullOrEmpty()) {
                getRecipesFromAPI()

            } else {
                log("readCachedRecipes")
                hideShimmerEffect()
                recipesAdapter.setRecipes(recipes[0].foodRecipe)
            }
        })
    }


    private fun getRecipesFromAPI() {
        log("getRecipesFromAPI")
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
        queries[ApiQuery.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[ApiQuery.QUERY_API_KEY] = Constants.API_KEY
        queries[ApiQuery.QUERY_TYPE] = Constants.DEFAULT_MEAL_TYPE
        queries[ApiQuery.QUERY_DIET] = Constants.DEFAULT_DIET_TYPE
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
    override fun onDestroy() {
        super.onDestroy()
        _binding = null //avoid memory leaks
    }

}