package com.alexkuz.foodrecipes.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.data.database.entities.FavouritesEntity
import com.alexkuz.foodrecipes.databinding.FavouritesRecipesRowLayoutBinding
import com.alexkuz.foodrecipes.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.alexkuz.foodrecipes.util.RecipesDiffUtil
import com.alexkuz.foodrecipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar


class FavouriteRecipeAdapter(
    private val requireActivity: FragmentActivity,
    private val mViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavouriteRecipeAdapter.MyViewHolder>(),
    ActionMode.Callback {

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavouritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    private lateinit var rootView: View
    var oldFavoriteRecipesList = emptyList<FavouritesEntity>()

    private lateinit var mActionMode: ActionMode

    class MyViewHolder(val binding: FavouritesRecipesRowLayoutBinding) :
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
        rootView = holder.itemView.rootView
        myViewHolders.add(holder)
        val currentFavouritesEntity = oldFavoriteRecipesList[position]
        holder.bind(currentFavouritesEntity)

        /**
         * Single Click Listener
         * */
        holder.binding.favoriteRecipesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentFavouritesEntity)
            } else {
                val action =
                    FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(
                        currentFavouritesEntity.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }


        /**
         * Long Click Listener
         * */
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentFavouritesEntity)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavouritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.purple_500)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, strokeColor: Int) {
        holder.binding.favouriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> mActionMode.finish()
            1 -> mActionMode.title = "${selectedRecipes.size} item selected"
            else -> mActionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    override fun getItemCount(): Int = oldFavoriteRecipesList.size


    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favourite_recipe_menu) {
            selectedRecipes.forEach { mViewModel.deleteFavouriteRecipe(it) }
            showSnackBar("${selectedRecipes.size} Recipe/s removed")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavouritesEntityList: List<FavouritesEntity>) {
        val favouritesDiffUtil = RecipesDiffUtil(oldFavoriteRecipesList, newFavouritesEntityList)
        val diffUtilResult = DiffUtil.calculateDiff(favouritesDiffUtil)
        oldFavoriteRecipesList = newFavouritesEntityList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}
            .show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}