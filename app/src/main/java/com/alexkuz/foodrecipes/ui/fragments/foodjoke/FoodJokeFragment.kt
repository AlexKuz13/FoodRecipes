package com.alexkuz.foodrecipes.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.databinding.FragmentFoodJokeBinding
import com.alexkuz.foodrecipes.util.NetworkResult
import com.alexkuz.foodrecipes.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private val mViewModel by viewModels<MainViewModel>()

    private var _binding: FragmentFoodJokeBinding? = null
    private val mBinding get() = _binding!!

    var foodJokeText = "Not found"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.mViewModel = mViewModel

        mBinding.foodJokeTextView.movementMethod = ScrollingMovementMethod()

        setHasOptionsMenu(true)

        mViewModel.getFoodJoke()
        mViewModel.foodJokeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    mBinding.foodJokeTextView.text = response.data?.text
                    if (response.data != null)
                        foodJokeText = response.data.text
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("FoodJokeLoading", "Loading")
                }

            }

        })

        return mBinding.root
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mViewModel.readFoodJoke.observe(viewLifecycleOwner, { database ->
                if (!database.isNullOrEmpty()) {
                    mBinding.foodJokeTextView.text = database[0].foodJoke.text
                    foodJokeText = database[0].foodJoke.text
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJokeText)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}