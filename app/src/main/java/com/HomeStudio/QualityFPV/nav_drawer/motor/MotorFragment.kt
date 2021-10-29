package com.HomeStudio.QualityFPV.nav_drawer.motor

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

class MotorFragment : ScrapingFragment() {

    private lateinit var motorViewModel: MotorViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        motorViewModel = ViewModelProvider(this).get(MotorViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_motor, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_flight_motor)

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("motors", recyclerView, it)
                    "GetFpv" -> Log.d("out", "Switched to GetFpv")
                    "RaceDayQuads" -> Log.d("out", "Switched to RaceDayQuads")
                }
            }
        })

        return root
    }
}