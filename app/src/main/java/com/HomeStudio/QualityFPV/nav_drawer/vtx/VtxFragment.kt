package com.HomeStudio.QualityFPV.nav_drawer.vtx

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.*
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment
import com.HomeStudio.QualityFPV.nav_drawer.motor.MotorViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VtxFragment : ScrapingFragment() {

    private lateinit var vtxViewModel: VtxViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vtxViewModel = ViewModelProvider(this).get(VtxViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_vtx, container, false)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = root.findViewById(R.id.recycler_view_vtx)

        mFilterViewModel.setMin(9.99)

        when (mSiteSelectorViewModel.website.value.toString()) {
            "Pyro Drone" -> getProducts("analog-vtx", recyclerView, "Pyro Drone")
            "GetFpv" -> getProducts("fpv/video-transmitters", recyclerView, "GetFpv")
            "RaceDayQuads" -> getProducts("all-video-transmitters", recyclerView, "RaceDayQuads")
        }

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("analog-vtx", recyclerView, it)
                    "GetFpv" -> getProducts("fpv/video-transmitters", recyclerView, it)
                    "RaceDayQuads" -> getProducts("all-video-transmitters", recyclerView, it)
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