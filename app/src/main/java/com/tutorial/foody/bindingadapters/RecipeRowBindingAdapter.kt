package com.tutorial.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.tutorial.foody.R

class RecipeRowBindingAdapter {
    companion object {
        @BindingAdapter("numberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView, likes: Int) =
            apply { textView.text = likes.toString() }

        @BindingAdapter("numberOfMinutes")
        @JvmStatic
        fun setNumberOfMinute(textView: TextView, minutes: Int) =
            apply { textView.text = minutes.toString() }

        @BindingAdapter("isVegen")
        @JvmStatic
        fun setVegenColor(view: View, isVegen: Boolean) = apply {
            if (isVegen) {
                when (view) {
                    is TextView -> view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.green_400
                        )
                    )
                    is ImageView -> view.setColorFilter(
                        ContextCompat.getColor(
                            view.context,
                            R.color.green_500
                        )
                    )
                }
            }
        }
    }
}