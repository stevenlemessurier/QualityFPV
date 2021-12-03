package com.HomeStudio.QualityFPV.data.product

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

    fun getProduct(type: String, site: String): Flow<Product> {
        return productDao.getProduct(type, site)
    }

    fun deleteProducts(type: String){
        productDao.deleteProducts(type)
    }

    fun getTopProductByCat(pyroType: String, getfpvType: String, rdqType: String): Flow<Product>{
        return productDao.getTopProductByCat(pyroType, getfpvType, rdqType)
    }
}