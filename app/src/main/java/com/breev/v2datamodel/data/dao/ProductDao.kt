package com.breev.v2datamodel.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.data.datamodels.Product.Companion.COL_PRODUCT_ID
import com.breev.v2datamodel.data.datamodels.Product.Companion.COL_PRODUCT_NAME
import com.breev.v2datamodel.data.datamodels.Product.Companion.TAB_PRODUCT
import com.breev.v2datamodel.data.datamodels.Product.Companion.TAB_PRODUCT_FTS
import com.breev.v2datamodel.data.relations.ProductWithProductCategory

@Dao
interface ProductDao {

    // insert a new product to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product): Long // returns row-ID of inserted item

    // update an existing product in the database
    @Update
    suspend fun updateProduct(product: Product): Int // returns number of rows updated

    // delete a Product from the Database
    @Delete
    suspend fun deleteProduct(productC: Product): Int // returns number of rows deleted

    // convenience: insert or update product
    @Transaction
    suspend fun saveProduct(product: Product): InsertOrUpdate {
        var id: Long = addProduct(product)
        var insertUpdate = InsertOrUpdate.INSERT
        if (id == -1L){
            id = updateProduct(product).toLong()
            insertUpdate = InsertOrUpdate.UPDATE
        }
        return InsertOrUpdate(id, insertUpdate)
    }



    // get all Products
    @Query("SELECT * FROM $TAB_PRODUCT ORDER BY $COL_PRODUCT_NAME ASC")
    fun getAllProducts(): LiveData<List<Product>>


    // filter Product
    //@Query("SELECT * FROM $TAB_PRODUCT WHERE $COL_PRODUCT_NAME LIKE :search ORDER BY $COL_PRODUCT_NAME ASC")
    @Query("SELECT * FROM $TAB_PRODUCT JOIN $TAB_PRODUCT_FTS ON ($TAB_PRODUCT.$COL_PRODUCT_ID = $TAB_PRODUCT_FTS.docid) WHERE $TAB_PRODUCT_FTS MATCH :search") //ORDER BY $COL_PRODUCT_NAME ASC
    fun filterProducts(search: String): LiveData<List<Product>>

    //-- All documents for which the first token in column "title" begins with "lin".
    //SELECT * FROM docs WHERE body MATCH 'title: ^lin*';
    // Query the database for documents for which the term "linux" appears in the document title, and the term "problems" appears in either the title or body of the document.
    //SELECT * FROM docs WHERE docs MATCH 'title:linux problems';


    // get all Products with their full category
    @Query("SELECT * FROM $TAB_PRODUCT") //ORDER BY $COL_PRODUCT_CATEGORY_ORDER ASC
    fun getAllProductsWithProductCategory(): LiveData<List<ProductWithProductCategory>>

    @Query("SELECT * FROM $TAB_PRODUCT WHERE $COL_PRODUCT_ID = :productId")
    fun getProductWithProductCategory(productId: Long): LiveData<List<ProductWithProductCategory>>





    
}



