package com.HomeStudio.QualityFPV.nav_drawer.flight_controller

import android.app.AlertDialog
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.*
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductDatabase
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import java.lang.Exception
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

class FlightControllerFragment : ScrapingFragment() {

    private lateinit var flightControllerViewModel: FlightControllerViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        flightControllerViewModel = ViewModelProvider(this).get(FlightControllerViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_flight_controller, container, false)
        recyclerView = root.findViewById(R.id.recycler_view_flight_controller)
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)

        mFilterViewModel.setMin(19.99)

        if (mSiteSelectorViewModel.initialValue.value == false){
            getProducts("flight-controllers", recyclerView, "Pyro Drone")
            mSiteSelectorViewModel.setInitialVal(true)
        }
        else{
            when (mSiteSelectorViewModel.website.value.toString()) {
                "Pyro Drone" -> getProducts("flight-controllers", recyclerView, "Pyro Drone")
                "GetFpv" -> getProducts("electronics/flight-controllers", recyclerView, "GetFpv")
                "RaceDayQuads" -> getProducts("all-flight-controllers", recyclerView, "RaceDayQuads")
            }
        }


        mSiteSelectorViewModel.website.observe(viewLifecycleOwner, {
            if (this.isVisible) {
                when (it) {
                    "Pyro Drone" -> getProducts("flight-controllers", recyclerView, it)
                    "GetFpv" -> getProducts("electronics/flight-controllers", recyclerView, it)
                    "RaceDayQuads" -> getProducts("all-flight-controllers", recyclerView, it)
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