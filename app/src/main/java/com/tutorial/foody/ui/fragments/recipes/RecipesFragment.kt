package com.tutorial.foody.ui.fragments.recipes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tutorial.foody.MainViewModel
import com.tutorial.foody.R
import com.tutorial.foody.data.network.NetworkResult
import com.tutorial.foody.databinding.FragmentRecipesBinding
import com.tutorial.foody.ui.fragments.recipes.adapter.RecipesAdapter
import com.tutorial.foody.utils.NetworkListener
import com.tutorial.foody.utils.log
import com.tutorial.foody.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val args by navArgs<RecipesFragmentArgs>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val recipesAdapter by lazy { RecipesAdapter() }
    private lateinit var networkListener: NetworkListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        setHasOptionsMenu(true)
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
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { networkStatus ->
                    log("network status: $networkStatus")
                    recipesViewModel.networkStatus = networkStatus
                    recipesViewModel.showNetworkStatus()
                    readCachedRecipes()

                }
        }
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })

        binding.fab.setOnClickListener {
            if (recipesViewModel.networkStatus)
                findNavController().navigate(R.id.action_recipesFragment_to_recipesFilterBottomSheet)
            else
                recipesViewModel.showNetworkStatus()
        }
    }

    private fun readCachedRecipes() = lifecycleScope.launchWhenStarted {
        mainViewModel.cachedRecipes.observeOnce(viewLifecycleOwner, { recipes ->
            if (recipes.isNotEmpty() && !args.backFromBottomSheet) {
                hideShimmerEffect()
                recipesAdapter.setRecipes(recipes[0].foodRecipe)
            } else
                getRecipesFromAPI()
        })
    }


    private fun getRecipesFromAPI() {
        log("getRecipesFromAPI")
        mainViewModel.getFoodRecipes(recipesViewModel.applyQueries())
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

    private fun loadRecipesFromCache() = lifecycleScope.launchWhenStarted {
        mainViewModel.cachedRecipes.observe(viewLifecycleOwner, { recipes ->
            if (recipes.isNotEmpty())
                recipesAdapter.setRecipes(recipes[0].foodRecipe)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

    }

    private fun searchRecipe(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchFoodRecipe(recipesViewModel.applySearchQueries(searchQuery))
        mainViewModel.searchRecipeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { recipesAdapter.setRecipes(foodRecipe) }
                }

                is NetworkResult.Error -> {
                    loadRecipesFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> showShimmerEffect()
            }

        })
    }


    private fun setupRecyclerView() = apply { binding.recipesRecyclerView.adapter = recipesAdapter }

    private fun showShimmerEffect() = binding.recipesRecyclerView.showShimmer()
    private fun hideShimmerEffect() = binding.recipesRecyclerView.hideShimmer()
    override fun onDestroy() {
        super.onDestroy()
        _binding = null //avoid memory leaks
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { searchRecipe(it) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}