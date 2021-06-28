package com.breev.v2datamodel.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breev.v2datamodel.data.repositories.ProductCategoryRepository
import com.breev.v2datamodel.data.repositories.ProductRepository

class RecipeViewModelFactory (private val productRepository: ProductRepository, private val productCategoryRepository: ProductCategoryRepository) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
                return RecipeViewModel(productRepository,productCategoryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

}
