package com.andrefpc.tvmazeclient.ui.show_details

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.data.Episode
import com.andrefpc.tvmazeclient.data.Season
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.databinding.LayoutHeaderShowBinding
import com.andrefpc.tvmazeclient.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.extensions.StringExtensions.removeHtmlTags
import org.koin.core.component.KoinComponent

/**
 * Adapter used to populate header of the show details screen
 */
class ShowHeaderAdapter(val context: Context, val show: Show, val isFavorite: Boolean ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var favoriteClickListener: (Show) -> Unit = { }

    fun onFavoriteClick(favoriteClickListener: (Show) -> Unit) {
        this.favoriteClickListener = favoriteClickListener
    }

    /**
     * Override method used to inflate the view of the adapter items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutHeaderShowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderViewHolder(binding)
    }

    /**
     * Override method to set the values for the adapter items widgets
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as HeaderViewHolder
        holder.binding.apply {
            image.loadImage(show.image?.original)
            name.text = show.name
            premiered.text = show.premiered ?: "Not Started"
            ended.text = show.ended ?: "Running"
            summary.text = show.summary.removeHtmlTags()
            genres.text = show.genres.joinToString()
            days.text = show.schedule.days.joinToString()
            time.text = show.schedule.time
            favorite.setOnClickListener {
                favoriteClickListener(show)
                changeFavorite(true)
            }

            changeFavorite(isFavorite)
        }

    }

    /**
     * Method to change the favorite icon tint regarding the status
     */
    private fun LayoutHeaderShowBinding.changeFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            favorite.setColorFilter(context.getColor(R.color.teal_200), PorterDuff.Mode.SRC_IN)
        } else {
            favorite.setColorFilter(context.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
        }
    }

    /**
     * Override method to get the total items the adapter
     */
    override fun getItemCount() = 1

    /**
     * View holder used to populate the item views
     */
    class HeaderViewHolder(val binding: LayoutHeaderShowBinding) : RecyclerView.ViewHolder(binding.root)
}
