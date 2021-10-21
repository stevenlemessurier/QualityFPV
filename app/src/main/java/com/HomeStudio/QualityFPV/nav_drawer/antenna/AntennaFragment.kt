package com.HomeStudio.QualityFPV.nav_drawer.antenna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment

class AntennaFragment : ScrapingFragment() {

    private lateinit var antennaViewModel: AntennaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        antennaViewModel = ViewModelProvider(this).get(AntennaViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_antenna, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_antenna)

        getPyroProducts("fpv-antennas", recyclerView)

        return root
    }
}