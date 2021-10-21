package com.HomeStudio.QualityFPV.nav_drawer.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.nav_drawer.ScrapingFragment

class CameraFragment : ScrapingFragment() {
    private lateinit var cameraViewModel: CameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_camera, container, false)
//        val textView: TextView = root.findViewById(R.id.text_camera)
//        cameraViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_camera)

        getPyroProducts("cameras-and-accessories", recyclerView)

        return root
    }
}