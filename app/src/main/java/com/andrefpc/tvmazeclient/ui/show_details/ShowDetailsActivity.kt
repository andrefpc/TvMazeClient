package com.andrefpc.tvmazeclient.ui.show_details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.databinding.ActivityShowDetailsBinding
import com.andrefpc.tvmazeclient.ui.episode_details.EpisodeModal
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDetailsBinding
    private val viewModel: ShowDetailsViewModel by viewModel()

    private val seasonEpisodeAdapter by lazy { SeasonEpisodeAdapter() }
    private val castAdapter by lazy { CastAdapter() }
    private var headerAdapter: ShowHeaderAdapter? = null
    private val concatAdapter by lazy { ConcatAdapter(headerAdapter, seasonEpisodeAdapter) }

    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()

        show = intent.getSerializableExtra("show") as Show
        viewModel.checkFavorite(show)
    }

    private fun initList() {

        headerAdapter?.onFavoriteClick {
            viewModel.addToFavorites(it)
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
        viewModel.getSeasons(show.id)
        viewModel.getCast(show.id)
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listSeasonEpisodes.observe(this) {
            seasonEpisodeAdapter.submitList(it)
        }

        viewModel.listCast.observe(this) {
            castAdapter.submitList(it)
        }

        viewModel.verifyFavorite.observe(this) {
            val show = intent.getSerializableExtra("show") as Show
            headerAdapter = ShowHeaderAdapter(this, show, it)
            initList()
        }

        viewModel.error.observe(this) {

        }

        viewModel.loading.observe(this) {
        }
    }
}