package com.HomeStudio.QualityFPV.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Keeps track of the website that is selected in the drawer to know where to scrape data from
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

    fun setInitialVal(){
        _initialVal.value = true
    }
}