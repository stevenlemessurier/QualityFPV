package com.HomeStudio.QualityFPV.nav_drawer.motor

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

class MotorFragment : ScrapingFragment() {

    private lateinit var motorViewModel: MotorViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        motorViewModel = ViewModelProvider(this).get(MotorViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_motor, container, false)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = root.findViewById(R.id.recycler_view_flight_motor)

        mFilterViewModel.setMin(8.49)

        when (mSiteSelectorViewModel.website.value.toString()) {
            "Pyro Drone" -> getProducts("motors", recyclerView, "Pyro Drone")
            "GetFpv" -> getProducts("motors/mini-quad-motors", recyclerView, "GetFpv")
            "RaceDayQuads" -> getProducts("all-motors", recyclerView, "RaceDayQuads")
        }

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("motors", recyclerView, it)
                    "GetFpv" -> getProducts("motors/mini-quad-motors", recyclerView, it)
                    "RaceDayQuads" -> getProducts("all-motors", recyclerView, it)
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