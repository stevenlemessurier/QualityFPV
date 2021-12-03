package com.HomeStudio.QualityFPV.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Determines the price range to scrape for products. Initial range is from $0 to $10,000
class FilterViewModel: ViewModel() {

    private val _min = MutableLiveData<Double>().apply{
        value = 0.0
    }

    private val _max = MutableLiveData<Double>().apply{
        value = 10000.0
    }

    private val _change = MutableLiveData<Boolean>().apply{
        value = false
    }

    val min: LiveData<Double> = _min
    val max: LiveData<Double> = _max
    val change: LiveData<Boolean> = _change

    fun setMin(minimum: Double){
        _min.value = minimum
    }

    fun setMax(maximum: Double){
        _max.value = maximum
    }

    fun setChange(setChange: Boolean){
        _change.value = setChange
    }
}