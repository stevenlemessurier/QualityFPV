package com.HomeStudio.QualityFPV.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Product>>

    private val repository: ProductRepository

    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
        readAllData = repository.readAllData
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProduct(product)
        }
    }

    fun readAllProductType(type: String): LiveData<List<Product>> {
        return repository.readAllProductType(type).asLiveData()
    }

    fun getAllProductNames(): LiveData<List<String>> {
        return repository.getAllProductNames().asLiveData()
    }

    fun getProduct(type: String): LiveData<Product> {
        return repository.getProduct(type).asLiveData()
    }

    fun deleteProducts(type: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProducts(type)
        }
    }
}