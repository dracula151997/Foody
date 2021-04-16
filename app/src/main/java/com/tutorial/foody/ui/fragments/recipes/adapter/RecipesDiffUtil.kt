package com.tutorial.foody.ui.fragments.recipes.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tutorial.foody.models.RecipeResult

class RecipesDiffUtil(
    private val oldList: List<RecipeResult>,
    private val newList: List<RecipeResult>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}