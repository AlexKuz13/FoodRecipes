package com.alexkuz.foodrecipes.adapters.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.models.Result
import com.alexkuz.foodrecipes.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.alexkuz.foodrecipes.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup

class RecipesRowBinding {

    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipesRowLayout: ConstraintLayout, result: Result) {
            recipesRowLayout.setOnClickListener {
                try {
                    val action =
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipesRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.i("TAG", e.toString())
                }
            }
        }

        @BindingAdapter("onFavouriteRecipeClickListener")
        @JvmStatic
        fun onFavouriteRecipeClickListener(recipesRowLayout: ConstraintLayout, result: Result) {
            recipesRowLayout.setOnClickListener {
                try {
                    val action =
                        FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(
                            result
                        )
                    recipesRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.i("TAG", e.toString())
                }
            }
        }


//        fun onFavouriteRecipeLongClickListener(
//            recipesRowLayout: ConstraintLayout,
//            requireActivity: FragmentActivity,
//            favouriteRecipeAdapter: FavouriteRecipeAdapter
//        ) {
//            recipesRowLayout.setOnLongClickListener {
//                requireActivity.startActionMode(favouriteRecipeAdapter.callback)
//                true
//            }
//
//        }

        @BindingAdapter("setIntToTextView")
        @JvmStatic
        fun setIntToTextView(textView: TextView, value: Int) {
            textView.text = value.toString()
        }


        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }


        @BindingAdapter("applyGreenColor")
        @JvmStatic
        fun applyVeganColor(view: View, diet: Boolean) {
            if (diet) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            } else {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.itemColor
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.itemColor
                            )
                        )
                    }
                }

            }
        }


        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?) {
            if (description != null) {
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }
}