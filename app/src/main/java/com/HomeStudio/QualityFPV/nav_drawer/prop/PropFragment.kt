package com.HomeStudio.QualityFPV.nav_drawer.prop

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
import com.HomeStudio.QualityFPV.nav_drawer.vtx.VtxViewModel

class PropFragment : ScrapingFragment() {

    private lateinit var propViewModel: PropViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        propViewModel = ViewModelProvider(this).get(PropViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_prop, container, false)
//        val textView: TextView = root.findViewById(R.id.text_prop)
//        propViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_prop)

        getPyroProducts("all-props/5", recyclerView)

        return root
    }
}