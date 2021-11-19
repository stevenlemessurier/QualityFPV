package com.HomeStudio.QualityFPV.nav_drawer.camera

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.*
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CameraFragment : ScrapingFragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var mFilterViewModel: FilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = root.findViewById(R.id.recycler_view_camera)

        mFilterViewModel.setMin(13.99)

        when (mSiteSelectorViewModel.website.value.toString()) {
            "Pyro Drone" -> getProducts("cameras-and-accessories", recyclerView, "Pyro Drone")
            "GetFpv" -> getProducts("fpv/cameras/flight-cameras", recyclerView, "GetFpv")
            "RaceDayQuads" -> getProducts("fpv-cameras", recyclerView, "RaceDayQuads")
        }

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("cameras-and-accessories", recyclerView, it)
                    "GetFpv" -> getProducts("fpv/cameras/flight-cameras", recyclerView, it)
                    "RaceDayQuads" -> getProducts("fpv-cameras", recyclerView, it)
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