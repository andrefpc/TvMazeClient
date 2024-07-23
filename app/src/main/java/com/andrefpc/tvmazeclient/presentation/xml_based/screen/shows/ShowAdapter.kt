package com.andrefpc.tvmazeclient.presentation.xml_based.screen.shows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.databinding.LayoutShowBinding
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState

/**
 * Adapter used to populate the shows list
 */
class ShowAdapter : ListAdapter<ShowViewState, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var clickListener: (ShowViewState) -> Unit = { }

    fun onClick(clickListener: (ShowViewState) -> Unit) {
        this.clickListener = clickListener
    }

    /**
     * Override method used to inflate the view of the adapter items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutShowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    /**
     * Override method to set the values for the adapter items widgets
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as MyViewHolder
        val show: ShowViewState = getItem(position)
        holder.binding.apply {
            image.loadImage(show.thumb)
            name.text = show.name
            genres.text = show.genres.joinToString()
            days.text = show.days.joinToString()
            time.text = show.time
            root.setOnClickListener {
                clickListener(show)
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
     * View holder used to populate the item views
     */
    class MyViewHolder(val binding: LayoutShowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private class ItemDiffCallback : DiffUtil.ItemCallback<ShowViewState>() {
            override fun areItemsTheSame(oldItem: ShowViewState, newItem: ShowViewState): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ShowViewState, newItem: ShowViewState): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.thumb == newItem.thumb &&
                        oldItem.image == newItem.image &&
                        oldItem.days == newItem.days &&
                        oldItem.thumb == newItem.time &&
                        oldItem.genres == newItem.genres &&
                        oldItem.summary == newItem.summary
        }
    }
}