package com.HomeStudio.QualityFPV.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.data.Review
import kotlinx.android.synthetic.main.adapter_product_reviews.view.*

// The adapter for a recycler view of reviews of a specific product
class ReviewAdapter(private val reviewList: MutableList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.adapter_product_reviews, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = reviewList[position]
        holder.itemView.review_title.text = currentItem.reviewTitle
        holder.itemView.review_details.text = currentItem.reviewDetails
        holder.itemView.review_date.text = currentItem.reviewDate

        // Show number of stars based on review rating
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