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
import com.HomeStudio.QualityFPV.data.Product
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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