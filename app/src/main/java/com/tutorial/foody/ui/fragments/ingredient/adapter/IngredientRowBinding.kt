package com.tutorial.foody.ui.fragments.ingredient.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import com.tutorial.foody.utils.Constants

class IngredientRowBinding {
    companion object {
        @BindingAdapter("setIngredientAmount")
        @JvmStatic
        fun setIngredientAmount(textView: TextView, amount: Double) {
            textView.text = amount.toString()
        }

        @BindingAdapter("loadIngredientImage")
        @JvmStatic
        fun setIngredientImage(imageView: ImageView, imagePath: String) =
            imageView.load(Constants.BASE_IMAGE_URL + imagePath) {
                crossfade(500)
                scale(Scale.FILL)
            }
    }
}