package com.breev.v2datamodel.data.repositories

import androidx.lifecycle.LiveData
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.data.dao.InsertOrUpdate
import com.breev.v2datamodel.data.dao.ProductCategoryDao
import com.breev.v2datamodel.data.relations.ProductCategoryWithProducts

class ProductCategoryRepository private constructor(private val productCategoryDao: ProductCategoryDao){

    // ------------------
    // Singleton Pattern
    // ------------------
    companion object {
        @Volatile private var INSTANCE: ProductCategoryRepository? = null
        fun getInstance(productCategoryDao: ProductCategoryDao): ProductCategoryRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductCategoryRepository(productCategoryDao).also { INSTANCE = it }
            }
    }

    fun getAllProductCategories(): LiveData<List<ProductCategory>> {
        return productCategoryDao.getAllProductCategories()
    }

    fun getAllProductCategoriesWithProducts(): LiveData<List<ProductCategoryWithProducts>> {
        return productCategoryDao.getAllProductCategoriesWithProducts()
    }

    fun getAllProductsOfProductCategory(productCategoryId: Long): LiveData<List<ProductCategoryWithProducts>> {
        return productCategoryDao.getAllProductsOfProductCategory(productCategoryId)
    }

    suspend fun saveProductCategory(productCategory: ProductCategory): InsertOrUpdate {
        return productCategoryDao.saveProductCategory(productCategory)
    }

    suspend fun deleteProductCategory(productCategory: ProductCategory): Int {
        return productCategoryDao.deleteProductCategory(productCategory)
    }


}