package com.alexkuz.foodrecipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexkuz.foodrecipes.data.database.entities.FavouritesEntity
import com.alexkuz.foodrecipes.databinding.FavouritesRecipesRowLayoutBinding
import com.alexkuz.foodrecipes.util.RecipesDiffUtil

class FavouriteRecipeAdapter : RecyclerView.Adapter<FavouriteRecipeAdapter.MyViewHolder>() {

    var oldFavoriteRecipesList = emptyList<FavouritesEntity>()

    class MyViewHolder(private var binding: FavouritesRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favouritesEntity: FavouritesEntity) {
            binding.favoritesEntity = favouritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FavouritesRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFavouritesEntity = oldFavoriteRecipesList[position]
        holder.bind(currentFavouritesEntity)
    }

    override fun getItemCount(): Int = oldFavoriteRecipesList.size

    fun setData(newFavouritesEntityList: List<FavouritesEntity>) {
        val favouritesDiffUtil = RecipesDiffUtil(oldFavoriteRecipesList, newFavouritesEntityList)
        val diffUtilResult = DiffUtil.calculateDiff(favouritesDiffUtil)
        oldFavoriteRecipesList = newFavouritesEntityList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}