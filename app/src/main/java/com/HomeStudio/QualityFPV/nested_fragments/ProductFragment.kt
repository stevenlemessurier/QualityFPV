package com.HomeStudio.QualityFPV.nested_fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.DetailImageAdapter
import com.HomeStudio.QualityFPV.adapters.ViewPagerAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_product.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class ProductFragment: Fragment() {

    lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        if(savedInstanceState != null)
            product = savedInstanceState.getParcelable<Product>("productData") as Product

        else{
            val bundle = this.arguments
            if(bundle != null)
                product = bundle.getParcelable<Product>("productData") as Product
        }

        Log.d("out", "Product Fragment opening")
    }



    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("productData", product)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            val root = inflater.inflate(R.layout.fragment_product, container, false)

            val viewPager = root.findViewById<ViewPager2>(R.id.viewPager)
            val tabLayout = root.findViewById<TabLayout>(R.id.tabLayout)

            val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, product)
            viewPager.adapter = adapter

            viewPager.offscreenPageLimit = 2

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Overview"
                    }
                    1 -> {
                        tab.text = "Reviews"
                    }
                    2 -> {
                        tab.text = "Videos"
                    }
                }
            }.attach()

            return root
    }
}