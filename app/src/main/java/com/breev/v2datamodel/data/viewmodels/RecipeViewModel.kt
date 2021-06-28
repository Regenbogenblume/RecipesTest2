package com.breev.v2datamodel.data.viewmodels

import androidx.lifecycle.*
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.data.dao.InsertOrUpdate
import com.breev.v2datamodel.data.repositories.ProductCategoryRepository
import com.breev.v2datamodel.data.repositories.ProductRepository
import com.breev.v2datamodel.utils.EasyLog
import kotlinx.coroutines.launch

class RecipeViewModel(private val productRepository: ProductRepository, private val productCategoryRepository: ProductCategoryRepository) : ViewModel() {

    companion object{
        private const val FILTER_WILDCARD_CHARACTER = "*"
    }

    private val LOG = EasyLog(javaClass.name)

    // Live Data Message on DB operation status
    private val _message: MutableLiveData<String> = MutableLiveData()
    fun getCurrentMessage(): LiveData<String> {
        LOG.d("function: ${this::getCurrentMessage}")
        return _message
    }


    // Live Data for filtering the products
    var productFilter: MutableLiveData<String> = MutableLiveData<String>("")
    fun setProductFilter(filter: String) {
        LOG.d("function: ${this::setProductFilter}: $filter")
        // optional: add wildcards to the filter
        productFilter.postValue(filter) // apply the filter
    }

    // Live Data Products
    private var _productsList: LiveData<List<Product>> =
            Transformations.switchMap(productFilter) { filter ->
                LOG.d("function: Transformation.switchMap: $filter")
                if (filter == null || filter == "") {
                    getAllProducts()
                } else {
                    // TODO: how to choose if wildcard or not?
                    filterProducts(sanitizeFilterQuery(filter, addWildcard = true) + "*")
                }
            }
    fun getProducts(): LiveData<List<Product>> {
        LOG.d("function: ${this::getProducts}")
        return _productsList
    }

    private fun sanitizeFilterQuery(filter: String, addWildcard: Boolean): String {
        var sanitizedFilter = filter
        sanitizedFilter = sanitizedFilter.replace(Regex.fromLiteral("\""), "\"\"") // escape double-quotes by using double double-quotes
        if (addWildcard) {
            sanitizedFilter = "$sanitizedFilter*"
        }
        sanitizedFilter = "\"$sanitizedFilter\"" // put query into double-quotes to escape - (the not operator in SQLite)
        sanitizedFilter = sanitizedFilter.replace("\\s".toRegex(), "* ") // replace whitespace by * to be able to search for multiple prefixes
        return sanitizedFilter
    }



    fun writeProductCategoryToDatabase(productCategory: ProductCategory){
        viewModelScope.launch {
            val insertOrUpdate = productCategoryRepository.saveProductCategory(productCategory)
            if (insertOrUpdate.insertUpdate == InsertOrUpdate.INSERT) {
                val id = insertOrUpdate.idOrCount
                if (id > -1) {
                    _message.value = "Product Category added successfully at index $id"
                } else {
                    _message.value = "Error inserting Product Category to database"
                }
            } else {
                val count = insertOrUpdate.idOrCount
                if (count > -1) {
                    _message.value = "$count Product Category(s) updated successfully"
                } else {
                    _message.value = "Error updating Product Category in database"
                }
            }
        }
    }

    fun deleteProductCategoryFromDatabase(productCategory: ProductCategory){
        viewModelScope.launch {
            val nrDeleted = productCategoryRepository.deleteProductCategory(productCategory)
            if (nrDeleted > 0) {
                _message.value = "$nrDeleted ProductCategory(s) deleted successfully"
            }else{
                _message.value = "Error deleting ProductCategory from database"
            }
        }
    }



    fun writeProductToDatabase(product: Product) {
        viewModelScope.launch {
            val insertOrUpdate = productRepository.saveProduct(product)
            if (insertOrUpdate.insertUpdate == InsertOrUpdate.INSERT) {
                val id = insertOrUpdate.idOrCount
                if (id > -1) {
                    _message.value = "Product added successfully at index $id"
                } else {
                    _message.value = "Error inserting Product to database"
                }
            } else {
                val count = insertOrUpdate.idOrCount
                if (count > -1) {
                    _message.value = "$count Product(s) updated successfully"
                } else {
                    _message.value = "Error updating Product in database"
                }
            }
        }
    }

    fun deleteProductFromDatabase(product: Product) {
        viewModelScope.launch {
            val nrDeleted = productRepository.deleteProduct(product)
            if (nrDeleted > 0) {
                _message.value = "$nrDeleted Product(s) deleted successfully"
            }else{
                _message.value = "Error deleting Product from database"
            }
        }
    }



    fun getAllProductCategories(): LiveData<List<ProductCategory>> {
        return productCategoryRepository.getAllProductCategories()
    }





    // used in Transformation.switchmap. But can be made public if need to use from outside this class
    private fun getAllProducts(): LiveData<List<Product>> {
        LOG.d("function: ${this::getAllProducts}")
         return productRepository.getAllProducts()
    }
    private fun filterProducts(search: String): LiveData<List<Product>>{
        LOG.d("function: ${this::filterProducts}: $search")
        return productRepository.filterProducts(search)
    }

}