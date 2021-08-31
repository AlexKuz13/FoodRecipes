package com.alexkuz.foodrecipes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alexkuz.foodrecipes.util.Constants


class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constants.QUERY_NUMBER] = "50"
        queries[Constants.QUERY_TYPE] = "main course"
        queries[Constants.QUERY_DIET] = "gluten free"
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}