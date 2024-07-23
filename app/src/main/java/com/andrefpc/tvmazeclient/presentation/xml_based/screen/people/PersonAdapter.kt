package com.andrefpc.tvmazeclient.presentation.xml_based.screen.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.databinding.LayoutPersonBinding
import com.andrefpc.tvmazeclient.presentation.model.PersonViewState
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage

/**
 * Adapter used to populate the people list
 */
class PersonAdapter : ListAdapter<PersonViewState, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var clickListener: (PersonViewState) -> Unit = { }

    fun onClick(clickListener: (PersonViewState) -> Unit) {
        this.clickListener = clickListener
    }

    /**
     * Override method used to inflate the view of the adapter items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutPersonBinding.inflate(
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
        val person: PersonViewState = getItem(position)
        holder.binding.apply {
            image.loadImage(person.thumb)
            name.text = person.name
            root.setOnClickListener {
                clickListener(person)
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
    class MyViewHolder(val binding: LayoutPersonBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private class ItemDiffCallback : DiffUtil.ItemCallback<PersonViewState>() {
            override fun areItemsTheSame(oldItem: PersonViewState, newItem: PersonViewState): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PersonViewState, newItem: PersonViewState): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.thumb == newItem.thumb &&
                        oldItem.image == newItem.image
        }
    }
}