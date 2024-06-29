package com.andrefpc.tvmazeclient.ui.xml_based.shows

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.extensions.ViewExtensions.hideKeyboard
import com.andrefpc.tvmazeclient.databinding.ActivityShowsBinding
import com.andrefpc.tvmazeclient.ui.xml_based.favorites.FavoritesActivity
import com.andrefpc.tvmazeclient.ui.xml_based.people.PeopleActivity
import com.andrefpc.tvmazeclient.ui.xml_based.show_details.ShowDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Shows screen of the application
 */
class ShowsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowsBinding
    private val viewModel: ShowsViewModel by viewModel()
    private val adapterShow by lazy { ShowAdapter() }
    private var onScrollListener: RecyclerView.OnScrollListener? = null
    private var showsLayoutManager: LinearLayoutManager? = null
    var needScroll = false
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
        if (needScroll) {
            binding.shows.scrollToPosition(0)
            needScroll = false
        }
    }

    /**
     * Lifecycle method that run when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "TV Shows"

        initObservers()
        initList()
        initListeners()

    }

    /**
     * Init the list of favorite shows
     */
    private fun initList() {
        showsLayoutManager = LinearLayoutManager(this@ShowsActivity)
        binding.shows.apply {
            layoutManager = showsLayoutManager
            adapter = adapterShow
        }
        listenScroll()
        viewModel.getShows()
    }

    /**
     * Init the listeners
     */
    private fun initListeners() {
        adapterShow.onClick {
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra("show", it)
            startActivity(intent)
        }

        adapterShow.registerAdapterDataObserver(listObserver)

        binding.search.onTextChange {
            if (it.length > 2) {
                needScroll = true
                viewModel.searchShows(it)
            }
            if (it.isEmpty()) {
                needScroll = true
                viewModel.getShows()
                binding.search.hideKeyboard()
            }
        }
        binding.search.onClear {
            needScroll = true
            viewModel.getShows()
            binding.search.hideKeyboard()
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

        viewModel.showEmpty.observe(this) {
            binding.showsShimmer.showEmpty()
        }

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

    /**
     * Lifecycle method that run when the activity is destroyed
     */
    override fun onDestroy() {
        removeScrollListener()
        adapterShow.unregisterAdapterDataObserver(listObserver)
        super.onDestroy()
    }

    /**
     * Listen scroll positions for pagination
     */
    private fun listenScroll() {
        onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!viewModel.searching) {
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

    /**
     * Remove the scroll listener
     */
    private fun removeScrollListener() {
        onScrollListener?.let {
            binding.shows.removeOnScrollListener(it)
            onScrollListener = null
        }
    }
}