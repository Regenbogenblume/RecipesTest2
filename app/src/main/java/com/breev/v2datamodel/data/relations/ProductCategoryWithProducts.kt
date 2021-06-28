package com.breev.v2datamodel.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.data.datamodels.ProductCategory.Companion.COL_PRODUCT_CATEGORY_ID

data class ProductCategoryWithProducts(

        @Embedded
    val productCategory: ProductCategory,

        @Relation(
        parentColumn = COL_PRODUCT_CATEGORY_ID,
        entityColumn = COL_PRODUCT_CATEGORY_ID
        //entity = Product::class
    )
    val products: List<Product>

)
