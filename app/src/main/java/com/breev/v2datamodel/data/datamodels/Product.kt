package com.breev.v2datamodel.data.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.breev.v2datamodel.data.datamodels.Product.Companion.COL_PRODUCT_NAME
import com.breev.v2datamodel.data.datamodels.Product.Companion.COL_PRODUCT_SEARCH_TERMS
import com.breev.v2datamodel.data.datamodels.Product.Companion.TAB_PRODUCT
import com.breev.v2datamodel.data.datamodels.Product.Companion.TAB_PRODUCT_FTS
import com.breev.v2datamodel.data.datamodels.ProductCategory.Companion.COL_PRODUCT_CATEGORY_ID
import java.io.Serializable

@Entity (tableName = TAB_PRODUCT)
data class Product(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_PRODUCT_ID)
    val id: Long = 0L,

    @ColumnInfo(name = COL_PRODUCT_NAME)
    val name: String,

    @ColumnInfo(name = COL_PRODUCT_SEARCH_TERMS)
    val searchTerms: String,

    // Foreign Key
    @ColumnInfo(name = COL_PRODUCT_CATEGORY_ID)
    val productCategoryId: Long,

) : Serializable {

    companion object{
        const val TAB_PRODUCT = "table_product"
        const val TAB_PRODUCT_FTS = TAB_PRODUCT + "_fts"
        const val COL_PRODUCT_ID = "product_id"
        const val COL_PRODUCT_NAME = "product_name"
        const val COL_PRODUCT_SEARCH_TERMS = "product_search_terms"
    }
}


@Fts4(contentEntity = Product::class)
@Entity(tableName = TAB_PRODUCT_FTS)
data class ProductFTS(

        @ColumnInfo (name = COL_PRODUCT_NAME)
        val name: String,

        @ColumnInfo (name = COL_PRODUCT_SEARCH_TERMS)
        val searchTerms: String

)