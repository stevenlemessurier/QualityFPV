package com.HomeStudio.QualityFPV.nav_drawer.prop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.SiteSelectorViewModel
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment
import com.HomeStudio.QualityFPV.nav_drawer.vtx.VtxViewModel

class PropFragment : ScrapingFragment() {

    private lateinit var propViewModel: PropViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        propViewModel = ViewModelProvider(this).get(PropViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_prop, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_prop)

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("all-props/5", recyclerView, it, 0.0)
                    "GetFpv" -> getProducts("propellers/mini-quad-propellers", recyclerView, it, 0.0)
                    "RaceDayQuads" -> getProducts("5-props", recyclerView, it, 0.0)
                }
            }
        })

        return root
    }
}