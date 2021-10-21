package com.HomeStudio.QualityFPV.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_product_images.view.*

// This class is the adapter for the recyclerview that displays the images of an item while being viewed
class DetailImageAdapter(private val imageList: MutableList<String>) :
    RecyclerView.Adapter<DetailImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_product_images, parent, false)
        )
    }

    // Load images to recycler view from list of image URIs passed in
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentImage = imageList[position]
        Glide.with(holder.itemView).load(currentImage).into(holder.itemView.image_detail)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}