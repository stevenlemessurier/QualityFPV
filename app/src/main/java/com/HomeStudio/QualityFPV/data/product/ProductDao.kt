package com.HomeStudio.QualityFPV.data.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Dao for Products to maintain Room Database
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun readAllData(): LiveData<List<Product>>

    @Query("SELECT * FROM product_table WHERE product_type = :type AND website = :site ORDER BY rating DESC")
    fun readAllProductType(type: String, site: String): Flow<List<Product>>

    @Query("SELECT * FROM product_table WHERE product_type = :type AND website = :site LIMIT 1")
    fun getProduct(type: String, site: String): Flow<Product>

    @Query("DELETE FROM product_table WHERE product_type = :type")
    fun deleteProducts(type: String)

    @Query("SELECT * FROM product_table WHERE product_type = :pyroType OR product_type = :getfpvType OR product_type = :rdqType ORDER BY rating DESC Limit 1")
    fun getTopProductByCat(pyroType: String, getfpvType: String, rdqType: String): Flow<Product>
}