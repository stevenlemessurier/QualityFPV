package com.HomeStudio.QualityFPV.nav_drawer.esc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EscViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the ESC fragment"
    }
    val text: LiveData<String> = _text
}