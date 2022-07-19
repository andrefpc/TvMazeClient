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

class ShowHeaderAdapter(val context: Context, val show: Show, val isFavorite: Boolean ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var favoriteClickListener: (Show) -> Unit = { }

    fun onFavoriteClick(favoriteClickListener: (Show) -> Unit) {
        this.favoriteClickListener = favoriteClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutHeaderShowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as HeaderViewHolder
        holder.binding.apply {
            image.loadImage(show.image?.original)
            name.text = show.name
            premiered.text = show.premiered ?: "Not Started"
            ended.text = show.ended ?: "Running"
            summary.text = show.summary.removeHtmlTags()
            favorite.setOnClickListener {
                favoriteClickListener(show)
                changeFavorite(true)
            }

            changeFavorite(isFavorite)
        }

    }

    private fun LayoutHeaderShowBinding.changeFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            favorite.setColorFilter(context.getColor(R.color.teal_700), PorterDuff.Mode.SRC_IN)
        } else {
            favorite.setColorFilter(context.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
        }
    }

    override fun getItemCount() = 1


    class HeaderViewHolder(val binding: LayoutHeaderShowBinding) : RecyclerView.ViewHolder(binding.root)
}
