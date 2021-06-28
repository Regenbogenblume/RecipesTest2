package com.breev.v2datamodel.data.repositories

import androidx.lifecycle.LiveData
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.data.dao.InsertOrUpdate
import com.breev.v2datamodel.data.dao.ProductDao
import com.breev.v2datamodel.data.relations.ProductWithProductCategory

class ProductRepository private constructor(private val productDao: ProductDao) {

    // ------------------
    // Singleton Pattern
    // ------------------
    companion object {
        @Volatile private var INSTANCE: ProductRepository? = null
        fun getInstance(productDao: ProductDao): ProductRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductRepository(productDao).also { INSTANCE = it }
            }
    }

    fun getAllProducts(): LiveData<List<Product>>{
        return productDao.getAllProducts()
    }

    fun filterProducts(search:String): LiveData<List<Product>>{
        return productDao.filterProducts(search)
    }

    fun getAllProductsWithProductCategory(): LiveData<List<ProductWithProductCategory>>{
        return productDao.getAllProductsWithProductCategory()
    }

    fun getProductWithProductCategory(productId: Long): LiveData<List<ProductWithProductCategory>>{
        return productDao.getProductWithProductCategory(productId)
    }

    suspend fun saveProduct(product: Product): InsertOrUpdate {
        return productDao.saveProduct(product)
    }

    suspend fun deleteProduct(product: Product): Int {
        return productDao.deleteProduct(product)
    }
}