package com.andrefpc.tvmazeclient.presentation.xml_based.screen.people

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.ActivityPeopleBinding
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.NavigatorScreen
import com.andrefpc.tvmazeclient.util.extensions.ViewExtensions.hideKeyboard
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * People screen of the application
 */
class PeopleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleBinding
    private val viewModel: PeopleViewModel by viewModel()
    private val appNavigation: AppNavigation by inject()
    private val adapterPerson by lazy { PersonAdapter() }
    private var onScrollListener: RecyclerView.OnScrollListener? = null
    private var showsLayoutManager: GridLayoutManager? = null
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
            binding.people.scrollToPosition(0)
            needScroll = false
        }
    }

    /**
     * Lifecycle method that run when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "People"

        initObservers()
        initList()
        initListeners()
    }

    /**
     * Init the list of favorite shows
     */
    private fun initList() {
        showsLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        binding.people.apply {
            layoutManager = showsLayoutManager
            adapter = adapterPerson
        }
        listenScroll()
        viewModel.getPeople()
    }

    /**
     * Init the listeners
     */
    private fun initListeners() {
        adapterPerson.onClick {
            if (it.image != null) {
                appNavigation.navigateTo(this, NavigatorScreen.PersonDetails(it))
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.enough_info_about_person, it.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        adapterPerson.registerAdapterDataObserver(listObserver)

        binding.search.onTextChange {
            if (it.length > 2) {
                needScroll = true
                viewModel.searchPeople(it)
            }
            if (it.isEmpty()) {
                needScroll = true
                viewModel.getPeople()
                binding.search.hideKeyboard()
            }
        }

        binding.search.onClear {
            needScroll = true
            viewModel.getPeople()
            binding.search.hideKeyboard()
        }
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.showEmpty.observe(this) {
            binding.peopleShimmer.showEmpty()
        }

        viewModel.listPeople.observe(this) {
            adapterPerson.submitList(it)
        }

        viewModel.addToListPeople.observe(this) {
            val currentList: MutableList<Person> = arrayListOf()
            currentList.addAll(adapterPerson.currentList)
            currentList.addAll(it)
            adapterPerson.submitList(currentList)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, "Error getting shows: ${it.message}", Toast.LENGTH_LONG).show()
        }

        viewModel.loading.observe(this) {
            if (it) binding.peopleShimmer.startProgress()
            else binding.peopleShimmer.stopProgress()
        }
    }

    /**
     * Lifecycle method that run when the activity is destroyed
     */
    override fun onDestroy() {
        removeScrollListener()
        adapterPerson.unregisterAdapterDataObserver(listObserver)
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
                            viewModel.getPeople()
                        }
                    }
                }
            }
        }
        onScrollListener?.let { binding.people.addOnScrollListener(it) }
    }

    /**
     * Remove the scroll listener
     */
    private fun removeScrollListener() {
        onScrollListener?.let {
            binding.people.removeOnScrollListener(it)
            onScrollListener = null
        }
    }
}