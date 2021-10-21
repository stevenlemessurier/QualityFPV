package com.HomeStudio.QualityFPV.nav_drawer.frame

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

class FrameFragment : ScrapingFragment() {
    private lateinit var frameViewModel: FrameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        frameViewModel = ViewModelProvider(this).get(FrameViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_frame, container, false)
//        val textView: TextView = root.findViewById(R.id.text_frame)
//        frameViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_frame)

        getPyroProducts("all-frames", recyclerView)

        return root
    }
}