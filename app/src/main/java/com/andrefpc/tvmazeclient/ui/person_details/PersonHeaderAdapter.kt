package com.andrefpc.tvmazeclient.ui.person_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.data.Person
import com.andrefpc.tvmazeclient.databinding.LayoutHeaderPersonBinding
import com.andrefpc.tvmazeclient.extensions.ImageViewExtensions.loadImage

/**
 * Adapter used to populate header of the person details screen
 */
class PersonHeaderAdapter(val person: Person) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showLabel = true

    fun hideLabel(){
        showLabel = false
        notifyItemChanged(0)
    }

    /**
     * Override method used to inflate the view of the adapter items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutHeaderPersonBinding.inflate(
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
            image.loadImage(person.image?.original)
            name.text = person.name
            if(showLabel){
                showsLabel.visibility = View.VISIBLE
            }else{
                showsLabel.visibility = View.GONE
            }
        }
    }

    /**
     * Override method to get the total items the adapter
     */
    override fun getItemCount() = 1

    /**
     * View holder used to populate the item views
     */
    class HeaderViewHolder(val binding: LayoutHeaderPersonBinding) :
        RecyclerView.ViewHolder(binding.root)
}
