package com.HomeStudio.QualityFPV.nav_drawer.frame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FrameViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Frame Fragment"
    }
    val text: LiveData<String> = _text
}