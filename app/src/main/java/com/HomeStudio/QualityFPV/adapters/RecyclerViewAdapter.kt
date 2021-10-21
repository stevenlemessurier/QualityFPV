package com.HomeStudio.QualityFPV.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.nested_fragments.ProductFragment
import kotlinx.android.synthetic.main.adapter_product_item.view.*

class RecyclerViewAdapter(private val mProductViewModel: ProductViewModel) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var productList = listOf<Product>()
    private lateinit var context: Context
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        this.parent = parent
        return ViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.adapter_product_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.itemView.product_name.text = currentItem.name
        holder.itemView.product_price.text = currentItem.price
        holder.itemView.rating.text = currentItem.rating.toString()
        Glide.with(holder.itemView).load(currentItem.img).into(holder.itemView.product_image)

        holder.itemView.product_card.setOnClickListener {
            val activity = context as AppCompatActivity
            val productFragment = ProductFragment()
            val bundle = Bundle()
            bundle.putParcelable("productData", currentItem)
            productFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, productFragment,"tag").addToBackStack("product").commit()
        }
    }

    fun setData(product: List<Product>) {
        this.productList = product
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}