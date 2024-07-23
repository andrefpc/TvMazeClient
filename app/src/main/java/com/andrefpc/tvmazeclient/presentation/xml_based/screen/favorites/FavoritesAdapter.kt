package com.andrefpc.tvmazeclient.presentation.xml_based.screen.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.databinding.LayoutFavoriteBinding
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage

/**
 * Adapter used to populate the favorites list
 */
class FavoritesAdapter : ListAdapter<ShowViewState, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var clickListener: (ShowViewState) -> Unit = { }
    private var deleteListener: (ShowViewState) -> Unit = { }

    fun onClick(clickListener: (ShowViewState) -> Unit) {
        this.clickListener = clickListener
    }

    fun onDelete(deleteListener: (ShowViewState) -> Unit) {
        this.deleteListener = deleteListener
    }

    /**
     * Override method used to inflate the view of the adapter items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutFavoriteBinding.inflate(
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
            delete.setOnClickListener {
                deleteListener(show)
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
    class MyViewHolder(val binding: LayoutFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private class ItemDiffCallback : DiffUtil.ItemCallback<ShowViewState>() {
            override fun areItemsTheSame(oldItem: ShowViewState, newItem: ShowViewState): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ShowViewState,
                newItem: ShowViewState
            ): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.image == newItem.image &&
                        oldItem.days == newItem.days &&
                        oldItem.time == newItem.time &&
                        oldItem.genres == newItem.genres &&
                        oldItem.summary == newItem.summary
        }
    }
}