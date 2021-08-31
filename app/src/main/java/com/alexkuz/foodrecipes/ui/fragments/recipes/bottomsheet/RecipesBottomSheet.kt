package com.alexkuz.foodrecipes.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexkuz.foodrecipes.databinding.RecipesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RecipesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: RecipesBottomSheetBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipesBottomSheetBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}