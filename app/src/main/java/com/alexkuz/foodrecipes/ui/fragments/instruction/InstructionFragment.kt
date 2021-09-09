package com.alexkuz.foodrecipes.ui.fragments.instruction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.alexkuz.foodrecipes.databinding.FragmentInstructionBinding
import com.alexkuz.foodrecipes.models.Result
import com.alexkuz.foodrecipes.util.Constants


class InstructionFragment : Fragment() {

    private var _binding: FragmentInstructionBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        val result: Result? = arguments?.getParcelable(Constants.RESULT_RECIPE_KEY)
        mBinding.instructionWebView.webViewClient = object : WebViewClient() {}
        val webSite = result!!.sourceUrl
        mBinding.instructionWebView.loadUrl(webSite)

        return mBinding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}