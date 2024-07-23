package com.andrefpc.tvmazeclient.presentation.xml_based.screen.person_details

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.ActivityPersonDetailsBinding
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.NavigatorScreen
import com.andrefpc.tvmazeclient.presentation.model.PersonViewState
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.shows.ShowAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Person details screen of the application
 */
class PersonDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonDetailsBinding
    private val viewModel: PersonDetailsViewModel by viewModel()
    private val appNavigation: AppNavigation by inject()
    private var headerAdapter: PersonHeaderAdapter? = null
    private val showAdapter by lazy { ShowAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(headerAdapter, showAdapter) }

    private var person: PersonViewState? = null

    /**
     * Lifecycle method that run when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        person = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("person", PersonViewState::class.java)
        } else {
            intent.getParcelableExtra("person")
        }

        person?.let {
            title = it.name
            headerAdapter = PersonHeaderAdapter(it)
        }

        initObservers()
        initList()
    }

    /**
     * Init the list of favorite shows
     */
    private fun initList() {
        showAdapter.onClick {
            appNavigation.navigateTo(this, NavigatorScreen.ShowDetails(it))
        }

        binding.personDetails.apply {
            layoutManager = LinearLayoutManager(this@PersonDetailsActivity)
            adapter = concatAdapter
        }
        person?.id?.let { viewModel.getShows(it) }
        binding.shimmerDetails.startProgress()
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listShows.observe(this) {
            if (it.isEmpty()) {
                headerAdapter?.hideLabel()
                Toast.makeText(
                    this,
                    getString(R.string.person_without_shows, person?.name),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showAdapter.submitList(it)
            }
            binding.shimmerDetails.stopProgress()
        }

        viewModel.error.observe(this) {
            Toast.makeText(
                this,
                getString(R.string.error_getting_shows, it.message),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}