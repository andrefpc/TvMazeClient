package com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.ActivityShowDetailsBinding
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.NavigatorScreen
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.episode_details.EpisodeModal
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Show details screen of the application
 */
class ShowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDetailsBinding
    private val viewModel: ShowDetailsViewModel by viewModel()
    private val appNavigation: AppNavigation by inject()
    private val seasonEpisodeAdapter by lazy { SeasonEpisodeAdapter() }
    private val castAdapter by lazy { CastAdapter() }
    private var headerAdapter: ShowHeaderAdapter? = null
    private val concatAdapter by lazy { ConcatAdapter(headerAdapter, seasonEpisodeAdapter) }

    var episodesLoaded = false
    var castLoaded = false

    private var show: ShowViewState? = null

    /**
     * Lifecycle method that run when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()

        show = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("show", ShowViewState::class.java) as ShowViewState
        } else {
            intent.getParcelableExtra("show")
        }

        show?.let {
            title = it.name
            viewModel.checkFavorite(it)
        }
    }

    /**
     * Init the list of favorite shows
     */
    private fun initList() {
        headerAdapter?.onFavoriteClick {
            viewModel.addToFavorites(it)
            Toast.makeText(
                this,
                getString(R.string.add_favorite_feedback, it.name),
                Toast.LENGTH_SHORT
            ).show()
        }

        seasonEpisodeAdapter.onEpisodeClick { episode ->
            val episodeModal = EpisodeModal.newInstance(episode = episode)
            episodeModal.show(supportFragmentManager, "EpisodeModal")
        }

        seasonEpisodeAdapter.onSeasonCLick { season ->
            viewModel.changeSeason(season.id)
        }

        binding.showDetails.apply {
            layoutManager = LinearLayoutManager(this@ShowDetailsActivity)
            adapter = concatAdapter
        }

        binding.cast.apply {
            layoutManager =
                LinearLayoutManager(this@ShowDetailsActivity, RecyclerView.HORIZONTAL, false)
            adapter = castAdapter
        }

        castAdapter.onCastClick {
            appNavigation.navigateTo(this, NavigatorScreen.PersonDetails(it.person))
        }

        show?.id?.let {
            viewModel.getSeasons(it)
            viewModel.getCast(it)
        }
        binding.shimmerDetails.startProgress()
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listSeasonEpisodes.observe(this) {
            seasonEpisodeAdapter.submitList(it)
            episodesLoaded = true
            stopShimmer()
        }

        viewModel.listCast.observe(this) {
            castAdapter.submitList(it)
            castLoaded = true
            stopShimmer()
        }

        viewModel.verifyFavorite.observe(this) {
            headerAdapter = this.show?.let { show -> ShowHeaderAdapter(this, show, it) }
            initList()
        }

        viewModel.error.observe(this) {
            Toast.makeText(
                this,
                getString(R.string.error_getting_shows, it.message),
                Toast.LENGTH_LONG
            ).show()
            binding.shimmerDetails.stopProgress()
        }
    }

    private fun stopShimmer() {
        if (episodesLoaded && castLoaded) {
            binding.shimmerDetails.stopProgress()
        }
    }
}