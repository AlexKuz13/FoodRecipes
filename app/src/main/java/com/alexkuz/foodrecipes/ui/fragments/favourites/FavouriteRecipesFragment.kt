package com.alexkuz.foodrecipes.ui.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.adapters.FavouriteRecipeAdapter
import com.alexkuz.foodrecipes.databinding.FragmentFavouriteRecipesBinding
import com.alexkuz.foodrecipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MainViewModel by viewModels()
    private val mAdapter: FavouriteRecipeAdapter by lazy {
        FavouriteRecipeAdapter(
            requireActivity(),
            mViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = this
        mBinding.mViewModel = mViewModel
        mBinding.mAdapter = mAdapter
        setHasOptionsMenu(true)
        setUpRecyclerView()
        return mBinding.root
    }

    private fun setUpRecyclerView() {
        mBinding.favoriteRecipesRecyclerView.adapter = mAdapter
        mBinding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            mViewModel.deleteAllFavouritesRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar() {
        Snackbar.make(
            mBinding.root,
            "All recipes removed.",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.clearContextualActionMode()
        _binding = null
    }
}