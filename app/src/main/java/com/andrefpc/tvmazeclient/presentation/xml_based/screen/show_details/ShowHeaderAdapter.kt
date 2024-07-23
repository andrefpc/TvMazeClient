package com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.LayoutHeaderShowBinding
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.util.extensions.StringExtensions.removeHtmlTags

/**
 * Adapter used to populate header of the show details screen
 */
class ShowHeaderAdapter(val context: Context, val show: ShowViewState, val isFavorite: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var favoriteClickListener: (ShowViewState) -> Unit = { }

    fun onFavoriteClick(favoriteClickListener: (ShowViewState) -> Unit) {
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
            image.loadImage(show.image)
            name.text = show.name
            premiered.text = show.premiered ?: "Not Started"
            ended.text = show.ended ?: "Running"
            summary.text = show.summary.removeHtmlTags()
            genres.text = show.genres.joinToString()
            days.text = show.days.joinToString()
            time.text = show.time
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
    class HeaderViewHolder(val binding: LayoutHeaderShowBinding) :
        RecyclerView.ViewHolder(binding.root)
}
