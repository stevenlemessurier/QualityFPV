package com.HomeStudio.QualityFPV.nav_drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.*
import com.HomeStudio.QualityFPV.data.FilterViewModel
import com.HomeStudio.QualityFPV.data.SiteSelectorViewModel
import com.HomeStudio.QualityFPV.dialog_fragements.FilterDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Displays top 10 ESCs found on one of the websites in a recycler view
class EscFragment : ScrapingFragment() {

    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(
            SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_esc, container, false)
        recyclerView = root.findViewById(R.id.recycler_view_esc)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)

        // Set default minimum price to scrape for antennas to $12.99
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

        // Open the filter dialog fragment to filter results by price
        fab.setOnClickListener{
            val filterDialog = FilterDialogFragment()
            filterDialog.show(parentFragmentManager, null)
        }

        return root
    }
}