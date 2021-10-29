package com.HomeStudio.QualityFPV.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    val readAllData: LiveData<List<Product>> = productDao.readAllData()

    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    fun readAllProductType(type: String, site: String): Flow<List<Product>> {
        return productDao.readAllProductType(type, site)
    }

    fun getAllProductNames(): Flow<List<String>> {
        return productDao.getAllProductNames()
    }

    fun getProduct(type: String, site: String): Flow<Product> {
        return productDao.getProduct(type, site)
    }

    fun deleteProducts(type: String){
        productDao.deleteProducts(type)
    }
}