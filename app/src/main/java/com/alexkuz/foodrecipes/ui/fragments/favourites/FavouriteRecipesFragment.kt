package com.alexkuz.foodrecipes.ui.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexkuz.foodrecipes.adapters.FavouriteRecipeAdapter
import com.alexkuz.foodrecipes.databinding.FragmentFavouriteRecipesBinding
import com.alexkuz.foodrecipes.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MainViewModel by viewModels()
    private val mAdapter: FavouriteRecipeAdapter by lazy { FavouriteRecipeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return mBinding.root
    }

    private fun setUpRecyclerView() {
        mBinding.favoriteRecipesRecyclerView.adapter = mAdapter
        mBinding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mViewModel.readFavouriteRecipes.observe(viewLifecycleOwner, {
            mAdapter.setData(it)
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}