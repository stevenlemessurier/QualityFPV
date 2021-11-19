package com.HomeStudio.QualityFPV.nav_drawer.esc

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

class EscFragment : ScrapingFragment() {

    private lateinit var escViewModel: EscViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        escViewModel = ViewModelProvider(this).get(EscViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_esc, container, false)
        recyclerView = root.findViewById(R.id.recycler_view_esc)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)

        mFilterViewModel.setMin(12.99)

        when (mSiteSelectorViewModel.website.value.toString()) {
            "Pyro Drone" -> getProducts("electronic-speed-controllers", recyclerView, "Pyro Drone")
            "GetFpv" -> getProducts("electronics/electronic-speed-controllers-esc", recyclerView, "GetFpv")
            "RaceDayQuads" -> getProducts("all-escs", recyclerView, "RaceDayQuads")
        }

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("electronic-speed-controllers", recyclerView, it)
                    "GetFpv" -> getProducts("electronics/electronic-speed-controllers-esc", recyclerView, it)
                    "RaceDayQuads" -> getProducts("all-escs", recyclerView, it)
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