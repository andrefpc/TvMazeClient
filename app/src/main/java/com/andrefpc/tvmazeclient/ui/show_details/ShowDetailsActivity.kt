package com.andrefpc.tvmazeclient.ui.show_details

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.databinding.ActivityShowDetailsBinding
import com.andrefpc.tvmazeclient.ui.episode_details.EpisodeModal
import com.andrefpc.tvmazeclient.ui.person_details.PersonDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Show details screen of the application
 */
class ShowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDetailsBinding
    private val viewModel: ShowDetailsViewModel by viewModel()

    private val seasonEpisodeAdapter by lazy { SeasonEpisodeAdapter() }
    private val castAdapter by lazy { CastAdapter() }
    private var headerAdapter: ShowHeaderAdapter? = null
    private val concatAdapter by lazy { ConcatAdapter(headerAdapter, seasonEpisodeAdapter) }

    var episodesLoaded = false
    var castLoaded = false

    private lateinit var show: Show

    /**
     * Lifecycle method that run when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()

        show = intent.getSerializableExtra("show") as Show
        title = show.name
        viewModel.checkFavorite(show)
    }

    /**
     * Init the list of favorite shows
     */
    private fun initList() {
        headerAdapter?.onFavoriteClick {
            viewModel.addToFavorites(it)
            Toast.makeText(this, getString(R.string.add_favorite_feedback, it.name), Toast.LENGTH_SHORT ).show()
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
            val intent = Intent(this, PersonDetailsActivity::class.java)
            intent.putExtra("person", it.person)
            startActivity(intent)
        }

        viewModel.getSeasons(show.id)
        viewModel.getCast(show.id)
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
            val show = intent.getSerializableExtra("show") as Show
            headerAdapter = ShowHeaderAdapter(this, show, it)
            initList()
        }

        viewModel.error.observe(this) {
            Toast.makeText(this,  getString(R.string.error_getting_shows, it.message), Toast.LENGTH_LONG).show()
            binding.shimmerDetails.stopProgress()
        }
    }

    private fun stopShimmer() {
        if (episodesLoaded && castLoaded) {
            binding.shimmerDetails.stopProgress()
        }
    }
}