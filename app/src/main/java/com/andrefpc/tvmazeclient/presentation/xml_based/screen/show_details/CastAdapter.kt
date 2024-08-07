package com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.databinding.LayoutCastBinding
import com.andrefpc.tvmazeclient.presentation.model.CastViewState

/**
 * Adapter used to populate the cast list
 */
class CastAdapter : ListAdapter<CastViewState, RecyclerView.ViewHolder>(ItemDiffCallback()) {
    private var castClickListener: (CastViewState) -> Unit = { }

    fun onCastClick(castClickListener: (CastViewState) -> Unit) {
        this.castClickListener = castClickListener
    }

    /**
     * Override method used to inflate the view of the adapter items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutCastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CastViewHolder(binding)
    }

    /**
     * Override method to set the values for the adapter items widgets
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is CastViewHolder -> {
                bindCastViewHolder(viewHolder.binding, currentList[position])
            }
        }
    }

    /**
     * Method used to set the values for the adapter items widgets
     */
    private fun bindCastViewHolder(binding: LayoutCastBinding, cast: CastViewState) {
        binding.apply {
            this.image.loadImage(cast.person.thumb)
            this.name.text = cast.person.name
            this.root.setOnClickListener {
                castClickListener(cast)
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
    class CastViewHolder(val binding: LayoutCastBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private class ItemDiffCallback : DiffUtil.ItemCallback<CastViewState>() {
            override fun areItemsTheSame(oldItem: CastViewState, newItem: CastViewState): Boolean =
                oldItem.person.id == newItem.person.id ||
                        oldItem.character.id == newItem.character.id

            override fun areContentsTheSame(oldItem: CastViewState, newItem: CastViewState): Boolean =
                oldItem.person.id == newItem.person.id &&
                        oldItem.person.name == newItem.person.name &&
                        oldItem.person.image == newItem.person.image &&
                        oldItem.character.id == newItem.character.id &&
                        oldItem.character.name == newItem.character.name
        }
    }
}