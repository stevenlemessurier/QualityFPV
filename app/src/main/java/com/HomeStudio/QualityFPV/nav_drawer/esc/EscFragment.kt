package com.HomeStudio.QualityFPV.nav_drawer.esc

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

class EscFragment : ScrapingFragment() {

    private lateinit var escViewModel: EscViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        escViewModel = ViewModelProvider(this).get(EscViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_esc, container, false)
//        val textView: TextView = root.findViewById(R.id.text_esc)
//        escViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_esc)

        getPyroProducts("electronic-speed-controllers", recyclerView)

        return root
    }
}