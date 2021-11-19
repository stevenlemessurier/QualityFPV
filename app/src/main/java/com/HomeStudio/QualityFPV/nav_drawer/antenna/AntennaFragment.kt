package com.HomeStudio.QualityFPV.nav_drawer.antenna

import android.content.res.Configuration
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
import com.HomeStudio.QualityFPV.*
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AntennaFragment : ScrapingFragment() {

    private lateinit var antennaViewModel: AntennaViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var mFilterViewModel: FilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        antennaViewModel = ViewModelProvider(this).get(AntennaViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_antenna, container, false)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = root.findViewById(R.id.recycler_view_antenna)

        mFilterViewModel.setMin(0.0)

        when (mSiteSelectorViewModel.website.value.toString()) {
            "Pyro Drone" -> getProducts("fpv-antennas", recyclerView, "Pyro Drone")
            "GetFpv" -> getProducts("fpv/antennas", recyclerView, "GetFpv")
            "RaceDayQuads" -> getProducts("video-transmitter-vtx-antennas", recyclerView, "RaceDayQuads")
        }

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("fpv-antennas", recyclerView, it)
                    "GetFpv" -> getProducts("fpv/antennas", recyclerView, it)
                    "RaceDayQuads" -> getProducts("video-transmitter-vtx-antennas", recyclerView, it)
                }
            }
        })

        fab.setOnClickListener{
            val filterDialog = FilterDialogFragment()
            filterDialog.show(parentFragmentManager, null)
        }

        return root
    }
}