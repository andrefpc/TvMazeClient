package com.andrefpc.tvmazeclient.ui.shows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.databinding.ActivityShowsBinding
import com.andrefpc.tvmazeclient.extensions.ViewExtensions.hideKeyboard
import com.andrefpc.tvmazeclient.ui.favorites.FavoritesActivity
import com.andrefpc.tvmazeclient.ui.people.PeopleActivity
import com.andrefpc.tvmazeclient.ui.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.show_details.ShowHeaderAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowsBinding
    private val viewModel: ShowsViewModel by viewModel()
    private val adapterShow by lazy { ShowAdapter() }
    private var onScrollListener: RecyclerView.OnScrollListener? = null
    private var showsLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "TV Shows"

        initObservers()
        initList()
        initListeners()

    }

    private fun initList() {
        showsLayoutManager = LinearLayoutManager(this@ShowsActivity)
        binding.shows.apply {
            layoutManager = showsLayoutManager
            adapter = adapterShow
        }
        listenScroll()
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
        binding.favorites.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
        binding.people.setOnClickListener {
            val intent = Intent(this, PeopleActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listShows.observe(this) {
            adapterShow.submitList(it)
        }

        viewModel.addToListShows.observe(this) {
            val currentList: MutableList<Show> = arrayListOf()
            currentList.addAll(adapterShow.currentList)
            currentList.addAll(it)
            adapterShow.submitList(currentList)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, "Error getting shows: ${it.message}", Toast.LENGTH_LONG).show()
        }

        viewModel.loading.observe(this) {
            if (it) binding.showsShimmer.startProgress()
            else binding.showsShimmer.stopProgress()
        }
    }

    override fun onDestroy() {
        removeScrollListener()
        super.onDestroy()
    }

    private fun listenScroll() {
        onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(!viewModel.searching) {
                    showsLayoutManager?.let { manager ->
                        val visibleItemCount: Int = manager.childCount
                        val totalItemCount: Int = manager.itemCount
                        val pastVisibleItems: Int = manager.findFirstVisibleItemPosition()
                        if (pastVisibleItems + visibleItemCount >= totalItemCount - 10) {
                            viewModel.currentPage++
                            viewModel.getShows()
                        }
                    }
                }
            }
        }
        onScrollListener?.let { binding.shows.addOnScrollListener(it) }
    }

    private fun removeScrollListener() {
        onScrollListener?.let {
            binding.shows.removeOnScrollListener(it)
            onScrollListener = null
        }
    }
}