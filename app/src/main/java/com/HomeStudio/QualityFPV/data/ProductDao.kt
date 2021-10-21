package com.HomeStudio.QualityFPV.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Dao for Products to maintain Room Database
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun readAllData(): LiveData<List<Product>>

    @Query("SELECT * FROM product_table WHERE product_type = :type ORDER BY rating DESC")
    fun readAllProductType(type: String): Flow<List<Product>>

    @Query("SELECT name FROM product_table")
    fun getAllProductNames(): Flow<List<String>>

    // Used to check for Existing products of type
    @Query("SELECT * FROM product_table WHERE product_type = :type LIMIT 1")
    fun getProduct(type: String): Flow<Product>

    @Query("DELETE FROM product_table WHERE product_type = :type")
    fun deleteProducts(type: String)
}