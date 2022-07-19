package com.andrefpc.tvmazeclient.ui.person_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.Person
import com.andrefpc.tvmazeclient.databinding.LayoutHeaderPersonBinding
import com.andrefpc.tvmazeclient.extensions.ImageViewExtensions.loadImage

class PersonHeaderAdapter(val person: Person) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutHeaderPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as HeaderViewHolder
        holder.binding.apply {
            image.loadImage(person.image?.original)
            name.text = person.name
        }

    }

    override fun getItemCount() = 1


    class HeaderViewHolder(val binding: LayoutHeaderPersonBinding) :
        RecyclerView.ViewHolder(binding.root)
}
