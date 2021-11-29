package com.HomeStudio.QualityFPV.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.*
import com.bumptech.glide.Glide
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.nested_fragments.ProductFragment
import kotlinx.android.synthetic.main.adapter_product_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Connection
import org.jsoup.Jsoup

class RecyclerViewAdapter(private val mProductViewModel: ProductViewModel) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var productList = listOf<Product>()
    private lateinit var context: Context
    private lateinit var parent: ViewGroup
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        this.parent = parent
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
        holder.itemView.rating_badge_text.text = "#${(position+1)}"

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

    private fun login(productUrl: String){
        val loginDialog = LoginDialogFragment(productUrl)
        val activity = context as MainActivity
        loginDialog.show(activity.supportFragmentManager, null)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}