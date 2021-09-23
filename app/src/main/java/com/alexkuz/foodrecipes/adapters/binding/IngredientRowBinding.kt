package com.alexkuz.foodrecipes.adapters.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.util.Constants
import java.util.*

class IngredientRowBinding {

    companion object {


        @BindingAdapter("loadIngredientImage")
        @JvmStatic
        fun loadIngredientImage(imageView: ImageView, imageUrl: String) {
            imageView.load(Constants.BASE_IMAGE_URL + imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("textCapitalize")
        @JvmStatic
        fun textCapitalize(textView: TextView, text: String) {
            textView.text = text.capitalize(Locale.ROOT)
        }

    }
}