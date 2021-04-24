package com.tutorial.foody.ui.fragments.ingredient.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import com.tutorial.foody.utils.Constants

class IngredientRowBinding {
    companion object {

        @BindingAdapter("loadIngredientImage")
        @JvmStatic
        fun setIngredientImage(imageView: ImageView, imagePath: String) =
            imageView.load(Constants.BASE_IMAGE_URL + imagePath) {
                crossfade(500)
                scale(Scale.FILL)
            }
    }
}