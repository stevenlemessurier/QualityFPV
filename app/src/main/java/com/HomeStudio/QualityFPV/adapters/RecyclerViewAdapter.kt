package com.HomeStudio.QualityFPV.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.dialog_fragements.LoginDialogFragment
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.data.product.Product
import com.HomeStudio.QualityFPV.nested_fragments.ProductFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_product_item.view.*

// The adapter for the recycler view that displays products in a scraping fragment
class RecyclerViewAdapter(private val isIdealDrone: Boolean) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var productList = listOf<Product>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.adapter_product_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.itemView.product_name.text = currentItem.name
        holder.itemView.product_price.text = currentItem.price
        Glide.with(holder.itemView).load(currentItem.img).into(holder.itemView.product_image)

        // Set product badge as numerical if the adapter is not being used for the ideal drone fragment
        if(!isIdealDrone){
            holder.itemView.rating_badge.visibility = View.VISIBLE
            holder.itemView.item_type_badge.visibility = View.GONE
            holder.itemView.rating_badge_text.text = "#${(position+1)}"
        }
        // Set product badge as product type if adapter is being used for ideal drone
        else{
            holder.itemView.rating_badge.visibility = View.GONE
            holder.itemView.item_type_badge.visibility = View.VISIBLE

            if(currentItem.product_type.contains("antennas"))
                holder.itemView.item_type_badge_text.text = "Top Antenna"
            else if(currentItem.product_type.contains("cameras"))
                holder.itemView.item_type_badge_text.text = "Top Camera"
            else if(currentItem.product_type.contains("electronic-speed-controllers") || currentItem.product_type.contains("esc"))
                holder.itemView.item_type_badge_text.text = "Top ESC"
            else if(currentItem.product_type.contains("flight-controllers"))
                holder.itemView.item_type_badge_text.text = "Top Flight Controller"
            else if(currentItem.product_type.contains("frames"))
                holder.itemView.item_type_badge_text.text = "Top Frame"
            else if(currentItem.product_type.contains("motors"))
                holder.itemView.item_type_badge_text.text = "Top Motor"
            else if(currentItem.product_type.contains("prop"))
                holder.itemView.item_type_badge_text.text = "Top Prop"
            else if(currentItem.product_type == "analog-vtx" || currentItem.product_type == "fpv/video-transmitters" || currentItem.product_type == "all-video-transmitters")
                holder.itemView.item_type_badge_text.text = "Top VTX"
        }

        // Open product fragment to display product details when product is clicked on
        holder.itemView.product_card.setOnClickListener {
            val activity = context as AppCompatActivity
            val productFragment = ProductFragment()
            val bundle = Bundle()
            bundle.putParcelable("productData", currentItem)
            productFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, productFragment,"tag").addToBackStack("product").commit()
        }

        holder.itemView.add_to_cart.setOnClickListener{
            login(currentItem.url)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(product: List<Product>) {
        this.productList = product
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    // Opens a login dialog fragment for the user to login to a website to add the product to their cart
    private fun login(productUrl: String){
        val loginDialog = LoginDialogFragment(productUrl)
        val activity = context as MainActivity
        loginDialog.show(activity.supportFragmentManager, null)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}