package com.alexkuz.foodrecipes.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.alexkuz.foodrecipes.MainViewModel
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.adapters.RecipesAdapter
import com.alexkuz.foodrecipes.databinding.FragmentRecipesBinding
import com.alexkuz.foodrecipes.util.Constants.Companion.API_KEY
import com.alexkuz.foodrecipes.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.alexkuz.foodrecipes.util.Constants.Companion.QUERY_API_KEY
import com.alexkuz.foodrecipes.util.Constants.Companion.QUERY_DIET
import com.alexkuz.foodrecipes.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.alexkuz.foodrecipes.util.Constants.Companion.QUERY_NUMBER
import com.alexkuz.foodrecipes.util.Constants.Companion.QUERY_TYPE
import com.alexkuz.foodrecipes.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val mBinding get() = _binding!!
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(layoutInflater, container, false)

        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setupRecyclerView()
        requestApiData()
        return mBinding.root


    }


    private fun requestApiData() {
        mViewModel.getRecipes(applyQueries())
        mViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_TYPE] = "finger food"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    private fun setupRecyclerView() {
        mBinding.recyclerView.adapter = mAdapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        showShimmerEffect()
    }


    private fun showShimmerEffect() {
        mBinding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        mBinding.recyclerView.hideShimmer()
    }

}