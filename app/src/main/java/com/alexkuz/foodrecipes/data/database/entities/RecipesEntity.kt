package com.alexkuz.foodrecipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexkuz.foodrecipes.models.FoodRecipe
import com.alexkuz.foodrecipes.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}