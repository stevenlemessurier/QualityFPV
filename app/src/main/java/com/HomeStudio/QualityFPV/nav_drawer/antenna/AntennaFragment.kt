package com.HomeStudio.QualityFPV.nav_drawer.antenna

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.SiteSelectorViewModel
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment

class AntennaFragment : ScrapingFragment() {

    private lateinit var antennaViewModel: AntennaViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        antennaViewModel = ViewModelProvider(this).get(AntennaViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_antenna, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_antenna)

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("fpv-antennas", recyclerView, it, 0.0)
                    "GetFpv" -> getProducts("fpv/antennas", recyclerView, it, 0.0)
                    "RaceDayQuads" -> getProducts("video-transmitter-vtx-antennas", recyclerView, it, 0.0)
                }
            }
        })

        return root
    }
}