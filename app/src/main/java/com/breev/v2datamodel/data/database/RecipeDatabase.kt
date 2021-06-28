package com.breev.v2datamodel.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.breev.v2datamodel.data.dao.ProductCategoryDao
import com.breev.v2datamodel.data.dao.ProductDao
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.data.datamodels.ProductFTS

@Database(entities = [Product::class,
                        ProductFTS::class,
                        ProductCategory::class],
        version = 4,
        exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    abstract val productCategoryDao: ProductCategoryDao
    abstract val productDao: ProductDao

    // ------------------
    // Singleton Pattern
    // ------------------
    companion object {
        @Volatile private var INSTANCE: RecipeDatabase? = null
        fun getInstance(context: Context): RecipeDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, RecipeDatabase::class.java, "recipe_database")
                    //.allowMainThreadQueries() // just for testing. Database queries should not be executed on main thread
                    .fallbackToDestructiveMigration() // wipe and rebuild database on upgrade. Just for testing
                    .build()
                    .also { INSTANCE = it }
            }
    }

}