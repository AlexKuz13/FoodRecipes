package com.alexkuz.foodrecipes.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.adapters.RecipesAdapter
import com.alexkuz.foodrecipes.databinding.FragmentRecipesBinding
import com.alexkuz.foodrecipes.util.NetworkListener
import com.alexkuz.foodrecipes.util.NetworkResult
import com.alexkuz.foodrecipes.util.observeOnce
import com.alexkuz.foodrecipes.viewmodels.MainViewModel
import com.alexkuz.foodrecipes.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private val args by navArgs<RecipesFragmentArgs>()
    private var _binding: FragmentRecipesBinding? = null
    private val mBinding get() = _binding!!
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var networkListener: NetworkListener

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
        mBinding.lifecycleOwner = this
        mBinding.mViewModel = mViewModel
        setHasOptionsMenu(true)
        setupRecyclerView()


        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.i("TAG", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        mBinding.fabRecipes.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else recipesViewModel.showNetworkStatus()
        }
        return mBinding.root
    }

    private fun setupRecyclerView() {
        mBinding.recyclerView.adapter = mAdapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }


    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mViewModel.readRecipes.observeOnce(viewLifecycleOwner, {
                if (it.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(it[0].foodRecipe)
                    hideShimmerEffect()
                } else requestApiData()
            })
        }
    }

    private fun requestApiData() {
        mViewModel.getRecipes(recipesViewModel.applyQueries())
        mViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                    recipesViewModel.saveMealAndDietType()
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
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

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mViewModel.getRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
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


    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mViewModel.readRecipes.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    mAdapter.setData(it[0].foodRecipe)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        searchView?.setOnCloseListener(this)
    }

    private fun showShimmerEffect() {
        mBinding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        mBinding.recyclerView.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClose(): Boolean {
        requestApiData()
        return true
    }

}