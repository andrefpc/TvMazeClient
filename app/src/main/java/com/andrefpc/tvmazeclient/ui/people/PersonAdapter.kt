package com.andrefpc.tvmazeclient.ui.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.Person
import com.andrefpc.tvmazeclient.databinding.LayoutPersonBinding
import com.andrefpc.tvmazeclient.databinding.LayoutShowBinding
import com.andrefpc.tvmazeclient.extensions.ImageViewExtensions.loadImage

class PersonAdapter : ListAdapter<Person, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private var clickListener: (Person) -> Unit = { }

    fun onClick(clickListener: (Person) -> Unit) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

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

    override fun getItemCount(): Int {
        return currentList.size
    }

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