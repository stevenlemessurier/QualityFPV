package com.HomeStudio.QualityFPV

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SiteSelectorViewModel: ViewModel(){

    private val _website = MutableLiveData<String>().apply{
        value = "Pyro Drone"
    }

    private val _initialVal = MutableLiveData<Boolean>().apply{
        value = false
    }

    val website: LiveData<String> = _website
    val initialValue: LiveData<Boolean> = _initialVal

    fun setWebsite(site: String){
        _website.value = site
    }

    fun setInitialVal(isInitialized: Boolean){
        _initialVal.value = true
    }
}