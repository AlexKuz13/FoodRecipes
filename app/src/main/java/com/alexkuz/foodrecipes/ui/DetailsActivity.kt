package com.alexkuz.foodrecipes.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.alexkuz.foodrecipes.R
import com.alexkuz.foodrecipes.adapters.PagerAdapter
import com.alexkuz.foodrecipes.data.database.entities.FavouritesEntity
import com.alexkuz.foodrecipes.databinding.ActivityDetailsBinding
import com.alexkuz.foodrecipes.ui.fragments.ingredients.IngredientsFragment
import com.alexkuz.foodrecipes.ui.fragments.instruction.InstructionFragment
import com.alexkuz.foodrecipes.ui.fragments.overview.OverviewFragment
import com.alexkuz.foodrecipes.util.Constants
import com.alexkuz.foodrecipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    private var _binding: ActivityDetailsBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: MainViewModel

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setSupportActionBar(mBinding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instruction")

        val resultBundle = Bundle()
        resultBundle.putParcelable(Constants.RESULT_RECIPE_KEY, args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            supportFragmentManager,
            lifecycle
        )

        mBinding.viewPager.adapter = adapter
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val item = menu?.findItem(R.id.save_to_favourites_menu)
        checkSavedRecipe(item!!)
        return true
    }

    private fun checkSavedRecipe(item: MenuItem) {
        mViewModel.readFavouriteRecipes.observe(this, { favouritesEntity ->
            try {
                for (savedRecipe in favouritesEntity) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(item, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    } else {
                        changeMenuItemColor(item, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", e.message.toString())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.save_to_favourites_menu -> {
                if (!recipeSaved)
                    saveToFavouritesRecipes(item)
                else removeFromFavourites(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavouritesRecipes(item: MenuItem) {
        val favouritesEntity = FavouritesEntity(0, args.result)
        mViewModel.insertFavouriteRecipe(favouritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved")
        recipeSaved = true
    }

    private fun removeFromFavourites(item: MenuItem) {
        val favouritesEntity = FavouritesEntity(
            savedRecipeId,
            args.result
        )
        mViewModel.deleteFavouriteRecipe(favouritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Recipe removed")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            mBinding.detailsLayout,
            message, Snackbar.LENGTH_LONG
        ).setAction("Okay") {}
            .show()

    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}