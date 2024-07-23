package com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.databinding.LayoutEpisodeBinding
import com.andrefpc.tvmazeclient.databinding.LayoutSeasonBinding
import com.andrefpc.tvmazeclient.presentation.model.EpisodeViewState
import com.andrefpc.tvmazeclient.presentation.model.SeasonEpisodeViewState
import com.andrefpc.tvmazeclient.presentation.model.SeasonViewState
import com.andrefpc.tvmazeclient.presentation.xml_based.widget.AnimatedArrow

/**
 * Adapter used to populate the episodes/seasons list
 */
class SeasonEpisodeAdapter :
    ListAdapter<SeasonEpisodeViewState, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var episodeClickListener: (EpisodeViewState) -> Unit = { }
    private var seasonClickListener: (SeasonViewState) -> Unit = { }

    fun onEpisodeClick(episodeClickListener: (EpisodeViewState) -> Unit) {
        this.episodeClickListener = episodeClickListener
    }

    fun onSeasonCLick(seasonClickListener: (SeasonViewState) -> Unit) {
        this.seasonClickListener = seasonClickListener
    }

    /**
     * Override method used to inflate the view of the adapter items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SEASON -> {
                val binding = LayoutSeasonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SeasonViewHolder(binding)
            }

            else -> {
                val binding = LayoutEpisodeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EpisodeViewHolder(binding)
            }
        }
    }

    /**
     * Override method to set the values for the adapter items widgets
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is SeasonViewHolder -> {
                currentList[position].season?.let { bindSeasonViewHolder(viewHolder.binding, it) }
            }

            is EpisodeViewHolder -> {
                currentList[position].episode?.let { bindEpisodeViewHolder(viewHolder.binding, it) }
            }
        }
    }

    /**
     * Method used to set the values for the season items
     */
    private fun bindSeasonViewHolder(binding: LayoutSeasonBinding, season: SeasonViewState) {
        binding.apply {
            val seasonName = "Season ${season.number}"
            this.season.text = seasonName
            this.totalEpisodes.text = season.episodeOrder.toString()
            if (season.opened) {
                this.arrow.setState(AnimatedArrow.MORE, false)
            } else {
                this.arrow.setState(AnimatedArrow.LESS, false)
            }
            this.root.setOnClickListener {
                this.arrow.switchState()
                seasonClickListener(season)
            }
        }
    }

    /**
     * Method used to set the values for the episodes items
     */
    private fun bindEpisodeViewHolder(binding: LayoutEpisodeBinding, episode: EpisodeViewState) {
        binding.apply {
            this.image.loadImage(episode.thumb)
            this.episode.text = episode.seasonEpisode
            this.name.text = episode.name
            this.root.setOnClickListener {
                episodeClickListener(episode)
            }
        }
    }

    /**
     * Override method to get the total items the adapter
     */
    override fun getItemCount(): Int {
        return currentList.size
    }

    /**
     * Override method to set the view type
     */
    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item.season != null) SEASON else EPISODE
    }

    /**
     * View holders used to populate the item views
     */
    class SeasonViewHolder(val binding: LayoutSeasonBinding) : RecyclerView.ViewHolder(binding.root)
    class EpisodeViewHolder(val binding: LayoutEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private class ItemDiffCallback : DiffUtil.ItemCallback<SeasonEpisodeViewState>() {
            override fun areItemsTheSame(oldItem: SeasonEpisodeViewState, newItem: SeasonEpisodeViewState): Boolean =
                oldItem.episode?.id == newItem.episode?.id ||
                        oldItem.season?.id == newItem.season?.id

            override fun areContentsTheSame(
                oldItem: SeasonEpisodeViewState,
                newItem: SeasonEpisodeViewState
            ): Boolean =
                oldItem.season?.id == newItem.season?.id &&
                        oldItem.season?.number == newItem.season?.number &&
                        oldItem.season?.episodeOrder == newItem.season?.episodeOrder &&
                        oldItem.episode?.id == newItem.episode?.id &&
                        oldItem.episode?.name == newItem.episode?.name &&
                        oldItem.episode?.season == newItem.episode?.season &&
                        oldItem.episode?.seasonEpisode == newItem.episode?.seasonEpisode &&
                        oldItem.episode?.summary == newItem.episode?.summary &&
                        oldItem.episode?.thumb == newItem.episode?.thumb &&
                        oldItem.episode?.image == newItem.episode?.image &&
                        oldItem.episode?.airdate == newItem.episode?.airdate &&
                        oldItem.episode?.time == newItem.episode?.time
        }

        const val SEASON = 1
        const val EPISODE = 2
    }
}