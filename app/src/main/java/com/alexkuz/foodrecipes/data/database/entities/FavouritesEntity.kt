package com.alexkuz.foodrecipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexkuz.foodrecipes.models.Result
import com.alexkuz.foodrecipes.util.Constants.Companion.FAVOURITES_RECIPES_TABLE

@Entity(tableName = FAVOURITES_RECIPES_TABLE)
data class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)