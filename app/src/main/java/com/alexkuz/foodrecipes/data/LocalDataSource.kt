package com.alexkuz.foodrecipes.data

import com.alexkuz.foodrecipes.data.database.RecipesDao
import com.alexkuz.foodrecipes.data.database.entities.FavouritesEntity
import com.alexkuz.foodrecipes.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity) {
        recipesDao.insertFavouriteRecipe(favouritesEntity)
    }

    fun readFavoriteRecipe(): Flow<List<FavouritesEntity>> {
        return recipesDao.readFavouriteRecipes()
    }

    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity) {
        recipesDao.deleteFavouriteRecipe(favouritesEntity)
    }

    suspend fun deleteAllFavouritesRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }
}