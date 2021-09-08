package com.alexkuz.foodrecipes.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexkuz.foodrecipes.adapters.IngredientsAdapter
import com.alexkuz.foodrecipes.databinding.FragmentIngredientsBinding
import com.alexkuz.foodrecipes.models.Result
import com.alexkuz.foodrecipes.util.Constants


class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val mBinding get() = _binding!!
    private var result: Result? = null
    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        result = arguments?.getParcelable(Constants.RESULT_RECIPE_KEY)
        setUpRecyclerView()
        return mBinding.root
    }

    private fun setUpRecyclerView() {
        mBinding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.ingredientsRecyclerView.adapter = mAdapter
        result?.extendedIngredients?.let { ingredients ->
            mAdapter.setData(ingredients)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}