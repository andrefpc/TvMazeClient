package com.andrefpc.tvmazeclient.presentation.xml_based.screen.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.databinding.LayoutPersonBinding

/**
 * Adapter used to populate the people list
 */
class PersonAdapter : ListAdapter<Person, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var clickListener: (Person) -> Unit = { }

    fun onClick(clickListener: (Person) -> Unit) {
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
        val person: Person = getItem(position)
        holder.binding.apply {
            image.loadImage(person.image?.medium)
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
        private class ItemDiffCallback : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.image == newItem.image
        }
    }
}