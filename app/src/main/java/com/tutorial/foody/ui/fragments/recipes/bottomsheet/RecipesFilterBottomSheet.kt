package com.tutorial.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.tutorial.foody.databinding.FragmentFilterBottomSheetBinding
import com.tutorial.foody.ui.fragments.recipes.RecipesViewModel
import com.tutorial.foody.utils.Constants
import com.tutorial.foody.utils.log
import java.util.*

class RecipesFilterBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel
    private var mealTypeChip = Constants.DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = Constants.DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedDietTypeId, value.selectedMealTypeId)
            log("Data Store : $value")
        })
        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = checkedId
        }

        binding.dietChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = checkedId
        }

        binding.filterBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )

            val action =
                RecipesFilterBottomSheetDirections.actionRecipesFilterBottomSheetToRecipesFragment(
                    true
                )
            findNavController().navigate(action)
        }
    }

    private fun updateChip(selectedDietTypeId: Int, selectedMealTypeId: Int) {
        if (selectedDietTypeId != 0)
            binding.dietChipGroup.findViewById<Chip>(selectedDietTypeId).isChecked = true
        if (selectedMealTypeId != 0)
            binding.mealTypeChipGroup.findViewById<Chip>(selectedMealTypeId).isChecked = true

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //avoid memory leaks
    }
}