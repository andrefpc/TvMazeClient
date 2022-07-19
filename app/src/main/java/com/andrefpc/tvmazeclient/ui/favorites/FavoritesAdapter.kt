package com.andrefpc.tvmazeclient.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.databinding.LayoutFavoriteBinding
import com.andrefpc.tvmazeclient.databinding.LayoutShowBinding
import com.andrefpc.tvmazeclient.extensions.ImageViewExtensions.loadImage

/**
 * Adapter used to populate the favorites list
 */
class FavoritesAdapter : ListAdapter<Show, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var clickListener: (Show) -> Unit = { }
    private var deleteListener: (Show) -> Unit = { }

    fun onClick(clickListener: (Show) -> Unit) {
        this.clickListener = clickListener
    }

    fun onDelete(deleteListener: (Show) -> Unit) {
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
        val show: Show = getItem(position)
        holder.binding.apply {
            image.loadImage(show.image?.medium)
            name.text = show.name
            genres.text = show.genres.joinToString()
            days.text = show.schedule.days.joinToString()
            time.text = show.schedule.time
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
        private class ItemDiffCallback : DiffUtil.ItemCallback<Show>() {
            override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.image == newItem.image &&
                        oldItem.schedule == newItem.schedule &&
                        oldItem.genres == newItem.genres &&
                        oldItem.summary == newItem.summary
        }
    }
}