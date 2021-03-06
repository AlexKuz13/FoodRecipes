package com.alexkuz.foodrecipes.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexkuz.foodrecipes.databinding.FragmentOverviewBinding
import com.alexkuz.foodrecipes.models.Result
import com.alexkuz.foodrecipes.util.Constants


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val result: Result? = arguments?.getParcelable(Constants.RESULT_RECIPE_KEY)
        mBinding.result = result
        return mBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}