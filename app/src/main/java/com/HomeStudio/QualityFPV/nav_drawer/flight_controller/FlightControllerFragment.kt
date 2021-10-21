package com.HomeStudio.QualityFPV.nav_drawer.flight_controller

import android.os.Bundle
import android.os.Parcel
import android.util.Log
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
import com.HomeStudio.QualityFPV.data.ProductDatabase
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import java.lang.Exception
import kotlin.math.roundToInt
import kotlin.random.Random

class FlightControllerFragment : ScrapingFragment() {


    private lateinit var flightControllerViewModel: FlightControllerViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

            flightControllerViewModel =
                ViewModelProvider(this).get(FlightControllerViewModel::class.java)

            val root = inflater.inflate(R.layout.fragment_flight_controller, container, false)
            val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_flight_controller)

            getPyroProducts("flight-controllers", recyclerView)


//        mProductViewModel.getProduct("flight-controllers").observe(viewLifecycleOwner, {
//            if(it == null){
//                doAsync {
//                    val productList = getPyroProducts("flight-controllers")
//
//                    for (i in 0..9) {
//                        mProductViewModel.addProduct(productList[i])
//                    }
//                }
//            }
//        })
//
//        doAsync {
//            if (activity != null) {
//                uiThread {
//                    val productRecyclerViewAdapter = RecyclerViewAdapter(mProductViewModel)
//                    val productLinearLayoutManager = LinearLayoutManager(
//                        activity,
//                        LinearLayoutManager.VERTICAL,
//                        false
//                    )
//
//                    mProductViewModel.readAllProductType("flight-controllers").observe(viewLifecycleOwner, { product ->
//                            productRecyclerViewAdapter.setData(product)
//                        })
//
//                    recyclerView.layoutManager = productLinearLayoutManager
//                    recyclerView.adapter = productRecyclerViewAdapter
//                }
//            }
//        }

            return root
    }
}