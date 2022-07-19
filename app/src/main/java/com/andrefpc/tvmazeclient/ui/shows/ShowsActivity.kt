package com.andrefpc.tvmazeclient.ui.shows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefpc.tvmazeclient.databinding.ActivityShowsBinding
import com.andrefpc.tvmazeclient.extensions.ViewExtensions.hideKeyboard
import com.andrefpc.tvmazeclient.ui.favorites.FavoritesActivity
import com.andrefpc.tvmazeclient.ui.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.show_details.ShowHeaderAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowsBinding
    private val viewModel: ShowsViewModel by viewModel()
    private val adapterShow by lazy { ShowAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        initList()
        initListeners()
        binding.favorites.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initList() {
        binding.shows.apply {
            layoutManager = LinearLayoutManager(this@ShowsActivity)
            adapter = adapterShow
        }
        viewModel.getShows(0)
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
                viewModel.getShows(0)
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

        viewModel.error.observe(this) {

        }

        viewModel.loading.observe(this) {
        }
    }
}