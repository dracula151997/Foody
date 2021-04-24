package com.tutorial.foody.utils

class Constants {
    companion object {
        const val PREF_BACK_ONLINE = "backOnline"
        const val API_KEY = "2d20b585a4284034acb59c4bb4723338"
        const val BASE_URL = "https://api.spoonacular.com/"
        const val DATABASE_NAME = "foody_db"

        //Bottom sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val PREF_MEAL_TYPE = "mealType"
        const val PREF_MEAL_TYPE_ID = "mealTypeId"
        const val PREF_DIET_TYPE = "dietType"
        const val PREF_DIET_TYPE_ID = "dietTypeId"
        const val DATA_STORE_NAME = "foody_preference"
        const val RECIPE_RESULT = "recipe_result"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
    }
}