package com.andrefpc.tvmazeclient.ui.xml_based.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.ActivityFavoritesBinding
import com.andrefpc.tvmazeclient.core.extensions.ViewExtensions.hideKeyboard
import com.andrefpc.tvmazeclient.ui.xml_based.show_details.ShowDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Favorites screen of the application
 */
class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModel()
    private val adapterFavorites by lazy { FavoritesAdapter() }
    private val listObserver: RecyclerView.AdapterDataObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                checkScroll()
            }

            override fun onChanged() {
                checkScroll()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                checkScroll()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                checkScroll()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                checkScroll()
            }
        }
    }

    private fun checkScroll() {
        binding.shows.scrollToPosition(0)
    }

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

        adapterFavorites.registerAdapterDataObserver(listObserver)

        adapterFavorites.onDelete {
            viewModel.deleteFavorite(it)
            Toast.makeText(this, getString(R.string.delete_favorite_feedback, it.name), Toast.LENGTH_SHORT ).show()
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

        binding.search.onClear {
            viewModel.getFavorites()
            binding.search.hideKeyboard()
        }
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.showEmpty.observe(this) {
            binding.showsShimmer.showEmpty()
        }

        viewModel.listShows.observe(this) {
            adapterFavorites.submitList(it)
            binding.showsShimmer.stopProgress()
        }
    }

    override fun onDestroy() {
        adapterFavorites.unregisterAdapterDataObserver(listObserver)
        super.onDestroy()
    }
}