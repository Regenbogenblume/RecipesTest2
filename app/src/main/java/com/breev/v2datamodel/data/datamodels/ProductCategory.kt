package com.breev.v2datamodel.data.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breev.v2datamodel.data.datamodels.ProductCategory.Companion.TAB_PRODUCT_CATEGORY
import java.io.Serializable

@Entity (tableName = TAB_PRODUCT_CATEGORY)
data class ProductCategory(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_PRODUCT_CATEGORY_ID)
    val id: Long = 0L,

    @ColumnInfo(name = COL_PRODUCT_CATEGORY_NAME)
    val name: String,
    // ex.
    //  meat & fish
    //  diary
    //  veg & fruit
    //  nuts & seeds & dried fruits
    //  herbs & spices
    //  condiments

    @ColumnInfo(name = COL_PRODUCT_CATEGORY_ORDER)
    val order: Int = 0

    ) : Serializable {

    companion object {
        const val TAB_PRODUCT_CATEGORY = "table_product_category"
        const val COL_PRODUCT_CATEGORY_ID =  "product_category_id"
        const val COL_PRODUCT_CATEGORY_NAME =  "product_category_name"
        const val COL_PRODUCT_CATEGORY_ORDER =  "product_category_order"
    }
}
