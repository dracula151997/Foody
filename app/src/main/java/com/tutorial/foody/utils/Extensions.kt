package com.tutorial.foody.utils

import java.util.*

@ExperimentalStdlibApi
fun String.capitalizeFirstLetterForEachWord(): String =
    split(" ").joinToString(" ") { it.lowercase().capitalize(Locale.ROOT) }