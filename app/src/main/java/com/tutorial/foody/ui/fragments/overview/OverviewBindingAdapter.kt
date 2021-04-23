package com.tutorial.foody.ui.fragments.overview

import android.widget.CheckBox
import androidx.databinding.BindingAdapter

class OverviewBindingAdapter {
    companion object {
        @BindingAdapter("setCheckedAndEnabled")
        @JvmStatic
        fun setCheckedAndEnabled(checkBox: CheckBox, value: Boolean) {
            checkBox.isChecked = value
            checkBox.isEnabled = value
            checkBox.isClickable = false
        }
    }
}