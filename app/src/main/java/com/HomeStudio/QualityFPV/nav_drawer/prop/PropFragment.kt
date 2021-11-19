package com.HomeStudio.QualityFPV.nav_drawer.prop

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.*
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment
import com.HomeStudio.QualityFPV.nav_drawer.vtx.VtxViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PropFragment : ScrapingFragment() {

    private lateinit var propViewModel: PropViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        propViewModel = ViewModelProvider(this).get(PropViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_prop, container, false)
        recyclerView = root.findViewById(R.id.recycler_view_prop)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)

        mFilterViewModel.setMin(0.0)

        when (mSiteSelectorViewModel.website.value.toString()) {
            "Pyro Drone" -> getProducts("all-props/5", recyclerView, "Pyro Drone")
            "GetFpv" -> getProducts("propellers/mini-quad-propellers", recyclerView, "GetFpv")
            "RaceDayQuads" -> getProducts("5-props", recyclerView, "RaceDayQuads")
        }

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("all-props/5", recyclerView, it)
                    "GetFpv" -> getProducts("propellers/mini-quad-propellers", recyclerView, it)
                    "RaceDayQuads" -> getProducts("5-props", recyclerView, it)
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