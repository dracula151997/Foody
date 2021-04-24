package com.tutorial.foody.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tutorial.foody.utils.capitalizeFirstLetterForEachWord
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ExtendedIngredient(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("unit")
    val unit: String
) : Parcelable{
    @ExperimentalStdlibApi
    val capitalizeName = name.capitalizeFirstLetterForEachWord()

    val capitalizeConsistency = consistency.capitalize(Locale.ROOT)
}