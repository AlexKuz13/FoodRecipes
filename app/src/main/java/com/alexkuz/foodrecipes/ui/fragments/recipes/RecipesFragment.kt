package com.alexkuz.foodrecipes.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexkuz.foodrecipes.adapters.RecipesAdapter
import com.alexkuz.foodrecipes.databinding.FragmentRecipesBinding
import com.alexkuz.foodrecipes.util.NetworkResult
import com.alexkuz.foodrecipes.viewmodels.MainViewModel
import com.alexkuz.foodrecipes.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val mBinding get() = _binding!!
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        requestApiData()
        return mBinding.root


    }


    private fun requestApiData() {
        mViewModel.getRecipes(recipesViewModel.applyQueries())
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