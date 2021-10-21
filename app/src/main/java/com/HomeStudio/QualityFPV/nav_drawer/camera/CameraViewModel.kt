package com.HomeStudio.QualityFPV.nav_drawer.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Camera Fragment"
    }
    val text: LiveData<String> = _text
}