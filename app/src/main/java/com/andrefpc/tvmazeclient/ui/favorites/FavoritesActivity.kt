package com.andrefpc.tvmazeclient.ui.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefpc.tvmazeclient.databinding.ActivityFavoritesBinding
import com.andrefpc.tvmazeclient.databinding.ActivityShowsBinding
import com.andrefpc.tvmazeclient.extensions.ViewExtensions.hideKeyboard
import com.andrefpc.tvmazeclient.ui.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.shows.ShowAdapter
import com.andrefpc.tvmazeclient.ui.shows.ShowsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModel()
    private val adapterShow by lazy { ShowAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        title = "Favorites"
        setContentView(binding.root)
        initObservers()
        initList()
        initListeners()
    }

    private fun initList() {
        binding.shows.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = adapterShow
        }
        viewModel.getShows()
    }

    private fun initListeners() {
        adapterShow.onClick {
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra("show", it)
            startActivity(intent)
        }

        binding.search.onTextChange {
            if(it.length > 2){
                viewModel.searchShows(it)
            }
            if(it.isEmpty()){
                viewModel.getShows()
                binding.search.hideKeyboard()
            }
        }
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listShows.observe(this) {
            adapterShow.submitList(it)
        }
    }
}