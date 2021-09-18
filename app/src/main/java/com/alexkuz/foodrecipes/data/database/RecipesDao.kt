package com.alexkuz.foodrecipes.data.database

import androidx.room.*
import com.alexkuz.foodrecipes.data.database.entities.FavouritesEntity
import com.alexkuz.foodrecipes.data.database.entities.FoodJokeEntity
import com.alexkuz.foodrecipes.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table")
    fun readRecipes(): Flow<List<RecipesEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("SELECT * FROM favourites_recipes_table ORDER BY id ASC")
    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Delete
    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("DELETE FROM favourites_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_table")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

}