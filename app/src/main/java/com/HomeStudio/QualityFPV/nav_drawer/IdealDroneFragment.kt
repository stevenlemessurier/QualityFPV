package com.HomeStudio.QualityFPV.nav_drawer

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.data.product.Product
import com.HomeStudio.QualityFPV.data.product.ProductViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

// Displays the top product from each category and all websites to produce the "ideal" top rated drone
class IdealDroneFragment: Fragment() {

    private lateinit var mProductViewModel: ProductViewModel
    var topItems: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_ideal_drone, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view_ideal_drone)


        // Get the top product from each category and add it to topItems list to send to recycler view
        mProductViewModel.getTopProductByCat("fpv-antennas", "fpv/antennas", "video-transmitter-vtx-antennas").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        mProductViewModel.getTopProductByCat("cameras-and-accessories", "fpv/cameras/flight-cameras", "fpv-cameras").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        mProductViewModel.getTopProductByCat("electronic-speed-controllers", "electronics/electronic-speed-controllers-esc", "all-escs").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        mProductViewModel.getTopProductByCat("flight-controllers", "electronics/flight-controllers", "all-flight-controllers").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        mProductViewModel.getTopProductByCat("all-frames", "multi-rotor-frames/5-quad-frames", "5-frames").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        mProductViewModel.getTopProductByCat("motors", "motors/mini-quad-motors", "all-motors").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        mProductViewModel.getTopProductByCat("all-props/5", "propellers/mini-quad-propellers", "5-props").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        mProductViewModel.getTopProductByCat("analog-vtx", "fpv/video-transmitters", "all-video-transmitters").observe(
            viewLifecycleOwner,
            { product ->
                topItems.add(product)
            })

        // Display top drone components from topItems list using the layout based on device orientation
        doAsync {
            uiThread {
                lateinit var productRecyclerViewAdapter: RecyclerViewAdapter
                lateinit var productLinearLayoutManager: LinearLayoutManager

                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        productRecyclerViewAdapter = RecyclerViewAdapter(true)
                        productLinearLayoutManager = LinearLayoutManager(
                            activity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    }
                    Configuration.ORIENTATION_PORTRAIT -> {
                        productRecyclerViewAdapter = RecyclerViewAdapter(true)
                        productLinearLayoutManager = LinearLayoutManager(
                            activity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }
                }

                productRecyclerViewAdapter.setData(topItems)
                recyclerView.layoutManager = productLinearLayoutManager
                recyclerView.adapter = productRecyclerViewAdapter
            }
        }

        return root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
}