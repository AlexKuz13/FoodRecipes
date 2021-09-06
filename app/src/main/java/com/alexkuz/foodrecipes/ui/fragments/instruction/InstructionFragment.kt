package com.alexkuz.foodrecipes.ui.fragments.instruction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexkuz.foodrecipes.databinding.FragmentInstructionBinding


class InstructionFragment : Fragment() {

    private var _binding: FragmentInstructionBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}