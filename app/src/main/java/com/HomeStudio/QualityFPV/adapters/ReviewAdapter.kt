package com.HomeStudio.QualityFPV.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.data.Review
import com.HomeStudio.QualityFPV.nested_fragments.ProductFragment
import kotlinx.android.synthetic.main.adapter_product_item.view.*
import kotlinx.android.synthetic.main.adapter_product_reviews.view.*

class ReviewAdapter(private val reviewList: MutableList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        this.parent = parent
        return ViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.adapter_product_reviews, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = reviewList[position]
        holder.itemView.review_title.text = currentItem.reviewTitle
        holder.itemView.review_details.text = currentItem.reviewDetails
        holder.itemView.review_date.text = currentItem.reviewDate

        when (currentItem.reviewScore) {
            4.0 -> holder.itemView.star5.visibility = View.GONE
            3.0 -> {
                holder.itemView.star5.visibility = View.GONE
                holder.itemView.star4.visibility = View.GONE
            }
            2.0 -> {
                holder.itemView.star5.visibility = View.GONE
                holder.itemView.star4.visibility = View.GONE
                holder.itemView.star3.visibility = View.GONE
            }
            1.0 -> {
                holder.itemView.star5.visibility = View.GONE
                holder.itemView.star4.visibility = View.GONE
                holder.itemView.star3.visibility = View.GONE
                holder.itemView.star2.visibility = View.GONE
            }
        }

    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}