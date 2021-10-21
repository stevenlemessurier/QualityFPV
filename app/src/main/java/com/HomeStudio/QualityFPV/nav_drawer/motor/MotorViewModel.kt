package com.HomeStudio.QualityFPV.nav_drawer.motor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MotorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Motor Fragment"
    }
    val text: LiveData<String> = _text
}