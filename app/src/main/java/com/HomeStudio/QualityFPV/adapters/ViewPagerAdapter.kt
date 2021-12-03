package com.HomeStudio.QualityFPV.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.HomeStudio.QualityFPV.data.product.Product
import com.HomeStudio.QualityFPV.nested_fragments.OverviewFragment
import com.HomeStudio.QualityFPV.nested_fragments.ReviewsFragment
import com.HomeStudio.QualityFPV.nested_fragments.VideoFragment

// The view pager adapter for the product fragment that handles the position of the overview, review, and video fragment
// of a specific product
class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val product: Product
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putParcelable("productData", product)

        return when (position) {
            0 -> {
                OverviewFragment().apply {
                    arguments = bundle
                }
            }
            1 -> {
                ReviewsFragment().apply {
                    arguments = bundle
                }
            }
            2 -> {
                VideoFragment().apply {
                    arguments = bundle
                }
            }
            else -> {
                OverviewFragment()
            }
        }
    }
}