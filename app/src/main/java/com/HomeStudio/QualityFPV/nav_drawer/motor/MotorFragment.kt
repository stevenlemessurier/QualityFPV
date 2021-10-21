package com.HomeStudio.QualityFPV.nav_drawer.motor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment

class MotorFragment : ScrapingFragment() {

    private lateinit var motorViewModel: MotorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        motorViewModel = ViewModelProvider(this).get(MotorViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_motor, container, false)
//        val textView: TextView = root.findViewById(R.id.text_motor)
//        motorViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_flight_motor)

        getPyroProducts("motors", recyclerView)

        return root
    }
}