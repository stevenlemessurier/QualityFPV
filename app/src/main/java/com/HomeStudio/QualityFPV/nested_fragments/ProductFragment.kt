package com.HomeStudio.QualityFPV.nested_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.ViewPagerAdapter
import com.HomeStudio.QualityFPV.data.product.Product
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// A wrapper fragment for product overview, reviews, and video fragments
class ProductFragment: Fragment() {

    lateinit var product: Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // Get product data passed in or saved on orientation change
        if(savedInstanceState != null)
            product = savedInstanceState.getParcelable<Product>("productData") as Product

        else{
            val bundle = this.arguments
            if(bundle != null)
                product = bundle.getParcelable<Product>("productData") as Product
        }
    }


    // Save product data on orientation change
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("productData", product)
        super.onSaveInstanceState(outState)
    }


    // Setup tab layout and view pager for child fragments
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