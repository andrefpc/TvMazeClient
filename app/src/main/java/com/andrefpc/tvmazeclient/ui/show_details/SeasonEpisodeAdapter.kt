package com.andrefpc.tvmazeclient.ui.show_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.Episode
import com.andrefpc.tvmazeclient.data.Season
import com.andrefpc.tvmazeclient.data.SeasonEpisode
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.databinding.LayoutEpisodeBinding
import com.andrefpc.tvmazeclient.databinding.LayoutSeasonBinding
import com.andrefpc.tvmazeclient.databinding.LayoutShowBinding
import com.andrefpc.tvmazeclient.extensions.ImageViewExtensions.loadImage

class SeasonEpisodeAdapter: ListAdapter<SeasonEpisode, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var clickListener: (SeasonEpisode) -> Unit = { }

    fun onClick(clickListener: (SeasonEpisode) -> Unit) {
        this.clickListener = clickListener
    }

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

    private fun bindSeasonViewHolder(binding: LayoutSeasonBinding, season: Season){
        binding.apply {
            val seasonName = "Season ${season.number}"
            this.season.text = seasonName
            this.totalEpisodes.text = season.episodeOrder.toString()
        }
    }

    private fun bindEpisodeViewHolder(binding: LayoutEpisodeBinding, episode: Episode){
        binding.apply {
            val seasonName = if(episode.season > 9) "S${episode.season}" else "S0${episode.season}"
            val episodeNumber = if(episode.number > 9) "S${episode.number}" else "S0${episode.number}"
            val episodeFinalNumber = "$seasonName | $episodeNumber"
            this.image.loadImage(episode.image.medium)
            this.episode.text = episodeFinalNumber
            this.name.text = episode.name
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item.season != null) SEASON else EPISODE
    }

    class SeasonViewHolder(val binding: LayoutSeasonBinding) : RecyclerView.ViewHolder(binding.root)
    class EpisodeViewHolder(val binding: LayoutEpisodeBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private class ItemDiffCallback : DiffUtil.ItemCallback<SeasonEpisode>() {
            override fun areItemsTheSame(oldItem: SeasonEpisode, newItem: SeasonEpisode): Boolean =
                oldItem.episode?.id == newItem.episode?.id ||
                        oldItem.season?.id == newItem.season?.id

            override fun areContentsTheSame(oldItem: SeasonEpisode, newItem: SeasonEpisode): Boolean =
                oldItem.season?.id == newItem.season?.id &&
                        oldItem.season?.number == newItem.season?.number &&
                        oldItem.season?.episodeOrder == newItem.season?.episodeOrder &&
                        oldItem.episode?.id == newItem.episode?.id &&
                        oldItem.episode?.name == newItem.episode?.name &&
                        oldItem.episode?.number == newItem.episode?.number &&
                        oldItem.episode?.season == newItem.episode?.season &&
                        oldItem.episode?.summary == newItem.episode?.summary &&
                        oldItem.episode?.image == newItem.episode?.image
        }

        const val SEASON = 1
        const val EPISODE = 2
    }
}