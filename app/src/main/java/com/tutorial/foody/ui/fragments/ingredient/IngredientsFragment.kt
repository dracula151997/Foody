package com.tutorial.foody.ui.fragments.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tutorial.foody.databinding.FragmentIngredientBinding
import com.tutorial.foody.models.RecipeResult
import com.tutorial.foody.ui.fragments.ingredient.adapter.IngredientAdapter
import com.tutorial.foody.utils.Constants

class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!
    private var result: RecipeResult? = null
    private val ingredientAdapter by lazy { IngredientAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result = it.getParcelable(Constants.RECIPE_RESULT)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        result?.let { ingredientAdapter.setIngredients(it) }
    }

    private fun setup() = with(binding) {
        ingredientRecyclerView.adapter = ingredientAdapter
    }

    private fun showShimmerAnimation() = binding.ingredientRecyclerView.showShimmer()
    private fun hideShimmerAnimation() = binding.ingredientRecyclerView.hideShimmer()
}