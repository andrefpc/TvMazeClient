package com.andrefpc.tvmazeclient.ui.show_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.*
import com.andrefpc.tvmazeclient.databinding.LayoutCastBinding
import com.andrefpc.tvmazeclient.databinding.LayoutEpisodeBinding
import com.andrefpc.tvmazeclient.databinding.LayoutSeasonBinding
import com.andrefpc.tvmazeclient.databinding.LayoutShowBinding
import com.andrefpc.tvmazeclient.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.widget.AnimatedArrow

class CastAdapter: ListAdapter<Cast, RecyclerView.ViewHolder>(ItemDiffCallback()) {
    private var castClickListener: (Cast) -> Unit = { }

    fun onCastClick(castClickListener: (Cast) -> Unit) {
        this.castClickListener = castClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutCastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is CastViewHolder -> {
                bindCastViewHolder(viewHolder.binding, currentList[position])
            }
        }
    }

    private fun bindCastViewHolder(binding: LayoutCastBinding, cast: Cast){
        binding.apply {
            this.image.loadImage(cast.person.image?.medium)
            this.name.text = cast.person.name
            this.root.setOnClickListener {
                castClickListener(cast)
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class CastViewHolder(val binding: LayoutCastBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private class ItemDiffCallback : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean =
                oldItem.person.id == newItem.person.id ||
                        oldItem.character.id == newItem.character.id

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean =
                oldItem.person.id == newItem.person.id &&
                        oldItem.person.name == newItem.person.name &&
                        oldItem.person.image == newItem.person.image &&
                        oldItem.character.id == newItem.character.id &&
                        oldItem.character.name == newItem.character.name
        }
    }
}