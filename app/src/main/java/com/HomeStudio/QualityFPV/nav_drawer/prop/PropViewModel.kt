package com.HomeStudio.QualityFPV.nav_drawer.prop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PropViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Prop Fragment"
    }
    val text: LiveData<String> = _text
}