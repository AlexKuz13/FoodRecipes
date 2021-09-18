package com.alexkuz.foodrecipes.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.alexkuz.foodrecipes.data.Repository
import com.alexkuz.foodrecipes.data.database.entities.FavouritesEntity
import com.alexkuz.foodrecipes.data.database.entities.FoodJokeEntity
import com.alexkuz.foodrecipes.data.database.entities.RecipesEntity
import com.alexkuz.foodrecipes.models.FoodJoke
import com.alexkuz.foodrecipes.models.FoodRecipe
import com.alexkuz.foodrecipes.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    /** ROOM */

    var readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()
    var readFavouriteRecipes: LiveData<List<FavouritesEntity>> =
        repository.local.readFavoriteRecipe().asLiveData()
    var readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }
    }

    fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavouriteRecipe(favouritesEntity)
        }
    }

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }
    }

    fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavouriteRecipe(favouritesEntity)
        }
    }

    fun deleteAllFavouritesRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavouritesRecipes()
        }
    }

    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun getFoodJoke() = viewModelScope.launch {
        getFoodJokeSafeCall()
    }

    private suspend fun getFoodJokeSafeCall() {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke()
                foodJokeResponse.value = handleFoodJokeResponse(response)
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Food Joke not found")
            }
        } else foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }


    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        return when {
            response.message().toString().contains("timeout") ->
                NetworkResult.Error("Timeout")
            response.code() == 402 ->
                NetworkResult.Error("Api key limited")
            response.body()!!.results.isNullOrEmpty() ->
                NetworkResult.Error("Recipes not found")
            response.isSuccessful -> {
                val foodRecipes = response.body()!!
                offlineCacheRecipes(foodRecipes)
                NetworkResult.Success(foodRecipes)
            }
            else ->
                NetworkResult.Error(response.message())
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke> {
        return when {
            response.message().toString().contains("timeout") ->
                NetworkResult.Error("Timeout")
            response.code() == 402 ->
                NetworkResult.Error("Api key limited")
            response.isSuccessful -> {
                val foodJoke = response.body()!!
                offlineCacheFoodJoke(foodJoke)
                NetworkResult.Success(foodJoke)
            }
            else ->
                NetworkResult.Error(response.message())
        }

    }

    private fun offlineCacheRecipes(foodRecipes: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipes)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}