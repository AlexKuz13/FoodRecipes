package com.alexkuz.foodrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexkuz.foodrecipes.databinding.FragmentRecipesBinding


class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(layoutInflater, container, false)
        mBinding.recyclerView.showShimmer()
        return mBinding.root
    }

}