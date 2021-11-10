package com.HomeStudio.QualityFPV.nav_drawer.esc

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

class EscFragment : ScrapingFragment() {

    private lateinit var escViewModel: EscViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        escViewModel = ViewModelProvider(this).get(EscViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_esc, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_esc)

        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if(this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("electronic-speed-controllers", recyclerView, it, 12.99)
                    "GetFpv" -> getProducts("electronics/electronic-speed-controllers-esc", recyclerView, it, 12.99)
                    "RaceDayQuads" -> getProducts("all-escs", recyclerView, it, 12.99)
                }
            }
        })

        return root
    }
}