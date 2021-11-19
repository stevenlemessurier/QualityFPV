package com.HomeStudio.QualityFPV.nav_drawer.frame

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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FrameFragment : ScrapingFragment() {

    private lateinit var frameViewModel: FrameViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        frameViewModel = ViewModelProvider(this).get(FrameViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_frame, container, false)
        recyclerView = root.findViewById(R.id.recycler_view_frame)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)

        mFilterViewModel.setMin(19.99)

        when (mSiteSelectorViewModel.website.value.toString()) {
            "Pyro Drone" -> getProducts("all-frames", recyclerView, "Pyro Drone")
            "GetFpv" -> getProducts("multi-rotor-frames/5-quad-frames", recyclerView, "GetFpv")
            "RaceDayQuads" -> getProducts("5-frames", recyclerView, "RaceDayQuads")
        }

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("all-frames", recyclerView, it)
                    "GetFpv" -> getProducts("multi-rotor-frames/5-quad-frames", recyclerView, it)
                    "RaceDayQuads" -> getProducts("5-frames", recyclerView, it)
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