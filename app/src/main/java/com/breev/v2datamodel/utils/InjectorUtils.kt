package com.breev.v2datamodel.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.breev.v2datamodel.data.database.RecipeDatabase
import com.breev.v2datamodel.data.repositories.ProductCategoryRepository
import com.breev.v2datamodel.data.repositories.ProductRepository
import com.breev.v2datamodel.data.viewmodels.RecipeViewModel
import com.breev.v2datamodel.data.viewmodels.RecipeViewModelFactory

object InjectorUtils {

    fun provideRecipeViewModel(applicationContext: Context, activity: AppCompatActivity): RecipeViewModel {

        // Dao's
        val productDao = RecipeDatabase.getInstance(applicationContext).productDao
        val productCategoryDao = RecipeDatabase.getInstance(applicationContext).productCategoryDao

        // Repositories
        val productRepository = ProductRepository.getInstance(productDao)
        val productCategoryRepository = ProductCategoryRepository.getInstance(productCategoryDao)

        // View Model
        val recipeViewModelFactory =  RecipeViewModelFactory(productRepository, productCategoryRepository)
        return ViewModelProvider(activity, recipeViewModelFactory).get(RecipeViewModel::class.java)
    }


}