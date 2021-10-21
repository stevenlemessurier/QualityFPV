package com.HomeStudio.QualityFPV.nav_drawer.vtx

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
import com.HomeStudio.QualityFPV.nav_drawer.motor.MotorViewModel

class VtxFragment : ScrapingFragment() {

    private lateinit var vtxViewModel: VtxViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vtxViewModel = ViewModelProvider(this).get(VtxViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_vtx, container, false)
//        val textView: TextView = root.findViewById(R.id.text_vtx)
//        vtxViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_vtx)

        getPyroProducts("analog-vtx", recyclerView)

        return root
    }
}