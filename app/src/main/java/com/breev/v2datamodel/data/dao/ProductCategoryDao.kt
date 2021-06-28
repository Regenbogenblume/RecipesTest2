package com.breev.v2datamodel.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.data.datamodels.ProductCategory.Companion.COL_PRODUCT_CATEGORY_ID
import com.breev.v2datamodel.data.datamodels.ProductCategory.Companion.COL_PRODUCT_CATEGORY_ORDER
import com.breev.v2datamodel.data.datamodels.ProductCategory.Companion.TAB_PRODUCT_CATEGORY
import com.breev.v2datamodel.data.relations.ProductCategoryWithProducts

@Dao
interface ProductCategoryDao {

    // insert a new productCategory to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProductCategory(productCategory: ProductCategory): Long // returns row-ID of inserted item

    // update an existing productCategory in the database
    @Update
    suspend fun updateProductCategory(productCategory: ProductCategory): Int // returns number of rows updated

    // delete a ProductCategory from the Database
    @Delete
    suspend fun deleteProductCategory(productCategory: ProductCategory): Int // returns number of rows deleted

    // convenience: insert or update productCategory
    @Transaction
    suspend fun saveProductCategory(productCategory: ProductCategory): InsertOrUpdate {
        var id: Long = addProductCategory(productCategory)
        var insertUpdate = InsertOrUpdate.INSERT
        if (id == -1L){
            id = updateProductCategory(productCategory).toLong()
            insertUpdate = InsertOrUpdate.UPDATE
        }
        return InsertOrUpdate(id, insertUpdate)
    }


    // get all productCategories
    @Query("SELECT * FROM $TAB_PRODUCT_CATEGORY ORDER BY $COL_PRODUCT_CATEGORY_ORDER ASC")
    fun getAllProductCategories(): LiveData<List<ProductCategory>>

    // get all productCategories with their products
    @Transaction
    @Query("SELECT * FROM $TAB_PRODUCT_CATEGORY ORDER BY $COL_PRODUCT_CATEGORY_ORDER ASC")
    fun getAllProductCategoriesWithProducts(): LiveData<List<ProductCategoryWithProducts>>

    // get all Products for a productCategory
    @Transaction
    @Query("SELECT * FROM $TAB_PRODUCT_CATEGORY WHERE $COL_PRODUCT_CATEGORY_ID = :productCategoryId ORDER BY $COL_PRODUCT_CATEGORY_ORDER ASC")
    fun getAllProductsOfProductCategory(productCategoryId: Long): LiveData<List<ProductCategoryWithProducts>>


}