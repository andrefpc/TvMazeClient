package com.andrefpc.tvmazeclient.ui.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefpc.tvmazeclient.databinding.ActivityFavoritesBinding
import com.andrefpc.tvmazeclient.extensions.ViewExtensions.hideKeyboard
import com.andrefpc.tvmazeclient.ui.show_details.ShowDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Favorites screen of the application
 */
class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModel()
    private val adapterFavorites by lazy { FavoritesAdapter() }

    /**
     * Lifecycle method that run when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        title = "Favorites"
        setContentView(binding.root)
        initObservers()
        initList()
        initListeners()
    }

    /**
     * Init the list of favorite shows
     */
    private fun initList() {
        binding.shows.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = adapterFavorites
        }
        viewModel.getFavorites()
    }

    /**
     * Init the listeners
     */
    private fun initListeners() {
        adapterFavorites.onClick {
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra("show", it)
            startActivity(intent)
        }

        adapterFavorites.onDelete {
            viewModel.deleteFavorite(it)
        }

        binding.search.onTextChange {
            if(it.length > 2){
                viewModel.searchFavorites(it)
            }
            if(it.isEmpty()){
                viewModel.getFavorites()
                binding.search.hideKeyboard()
            }
        }
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listShows.observe(this) {
            adapterFavorites.submitList(it)
        }
    }
}