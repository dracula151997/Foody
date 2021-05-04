package com.tutorial.foody.ui.fragments.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.tutorial.foody.MainViewModel
import com.tutorial.foody.R
import com.tutorial.foody.databinding.FragmentFavoriteRecipesBinding
import com.tutorial.foody.ui.fragments.favorite.adapters.FavoriteRecipeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    private val favoriteRecipesAdapter by lazy {
        FavoriteRecipeAdapter(
            requireActivity(),
            mainViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.favoriteRecipeAdapter = favoriteRecipesAdapter
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all_recipes) {
            removeAllFavoriteRecipeFromDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeAllFavoriteRecipeFromDB() {
        mainViewModel.deleteAllFavoriteRecipes()
        showSnackBar(getString(R.string.all_recipes_deleted_msg))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.okay) {}.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        favoriteRecipesAdapter.closeActionModeIfShowed()
    }
}