package com.HomeStudio.QualityFPV.nav_drawer.vtx

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
import com.HomeStudio.QualityFPV.nav_drawer.motor.MotorViewModel

class VtxFragment : ScrapingFragment() {

    private lateinit var vtxViewModel: VtxViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vtxViewModel = ViewModelProvider(this).get(VtxViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_vtx, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_vtx)

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("analog-vtx", recyclerView, it)
                    "GetFpv" -> Log.d("out", "Switched to GetFpv")
                    "RaceDayQuads" -> Log.d("out", "Switched to RaceDayQuads")
                }
            }
        })

        return root
    }
}