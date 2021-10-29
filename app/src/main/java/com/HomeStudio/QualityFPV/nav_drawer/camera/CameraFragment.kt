package com.HomeStudio.QualityFPV.nav_drawer.camera

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.SiteSelectorViewModel
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment

class CameraFragment : ScrapingFragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_camera)

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("cameras-and-accessories", recyclerView, it)
                    "GetFpv" -> Log.d("out", "Switched to GetFpv")
                    "RaceDayQuads" -> Log.d("out", "Switched to RaceDayQuads")
                }
            }
        })

        return root
    }
}