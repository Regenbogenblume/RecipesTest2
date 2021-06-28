package com.breev.v2datamodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.data.viewmodels.RecipeViewModel
import com.breev.v2datamodel.databinding.ActivityEditProductBinding
import com.breev.v2datamodel.utils.EasyLog
import com.breev.v2datamodel.utils.InjectorUtils

class EditProductActivity : AppCompatActivity() {

    private val LOG = EasyLog(javaClass.name)
    private lateinit var binding: ActivityEditProductBinding
    private lateinit var viewModel: RecipeViewModel
    private var currentProduct: Product? = null
    
    companion object{
        const val INTENT_EXTRA_PRODUCT = "intent_extra_product"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        LOG.d("function: onCreate")
        
        //binding and layout
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        //viewModel
        viewModel = InjectorUtils.provideRecipeViewModel(application,this)
        initLiveData()

        // UI
        initUI()
        
    }


    // ---------------------------------------------------------------------
    // USER INTERFACE ------------------------------------------------------
    // ---------------------------------------------------------------------
    private fun initUI() {
        LOG.d("function: ${this::initUI}")
        getIntentData()
        productToUi(currentProduct)

        // Listener
        binding.btnSaveProduct.setOnClickListener { saveProduct() }
        binding.btnDeleteProduct.setOnClickListener { deleteProduct() }
        binding.btnDeleteProductCategory.setOnClickListener { emptyProductCategory() }

        binding.etProductName.addTextChangedListener(productTextWatcher)
        binding.etProductCategory.addTextChangedListener(productTextWatcher)
        binding.etProductCategory.addTextChangedListener(productCategoryTextWatcher)
    }

    private fun emptyProductCategory() {
        LOG.d("function: ${this::emptyProductCategory}")
        binding.etProductCategory.setText("")
    }

    private val productTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) { enableDisableSaveBtn() }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
    }

    private val productCategoryTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) { enableDisableDeleteCategoryBtn() }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
    }

    private fun enableDisableSaveBtn(){
        LOG.d("function: ${this::enableDisableSaveBtn}")
        // enable the Save Button only of the fields in the UI differ from the entry in the database
        val uiProduct = productFromUi()
        binding.btnSaveProduct.isEnabled = (uiProduct != currentProduct && isProductValid(uiProduct))
    }

    private fun enableDisableDeleteCategoryBtn(){
        LOG.d("function: ${this::enableDisableDeleteCategoryBtn}")
        val uiProductCategory = binding.etProductCategory.text
        binding.btnDeleteProductCategory.isEnabled = (uiProductCategory.isNotEmpty())
    }



    // ---------------------------------------------------------------------
    // ACTIVITY'S PURPOSE FUNCTIONS ----------------------------------------
    // ---------------------------------------------------------------------
    private fun saveProduct() {
        LOG.d("function: ${this::saveProduct}")
        val product = productFromUi()
        if (isProductValid(product)) {
            viewModel.writeProductToDatabase(product)
        } else {
            LOG.d("Product not valid")
        }
        finish()
    }

    private fun deleteProduct() {
        LOG.d("function: ${this::deleteProduct}")
        if (currentProduct != null) {
            viewModel.deleteProductFromDatabase(currentProduct!!)
        } else {
            LOG.d("nothing to delete")
        }
        finish()
    }

    private fun initLiveData(){
        LOG.d("function: ${this::initLiveData}")
        // observe LiveData for Toasting message on Database-success
        viewModel.getCurrentMessage().observe(this, { message ->
            LOG.d(message)
        })
    }


    // ---------------------------------------------------------------------
    // DATA & TYPE CHECKING ------------------------------------------------
    // ---------------------------------------------------------------------
    private fun getIntentData() {
        LOG.d("function: ${this::getIntentData}")
        val intent: Intent = intent
        if (intent.hasExtra(INTENT_EXTRA_PRODUCT)){
            val product: Product =
                intent.extras?.get(INTENT_EXTRA_PRODUCT) as Product
            currentProduct = product
        }
        LOG.d("currentProduct: $currentProduct")
    }

    private fun productToUi(product: Product?){
        LOG.d("function: ${this::productToUi}")
        if (product!=null) {
            binding.tvProductId.text = product.id.toString()
            binding.etProductName.setText(product.name)
            binding.etProductCategory.setText(product.productCategoryId.toString())
            binding.etProductSearchTerms.setText(product.searchTerms)
            binding.btnDeleteProduct.isEnabled = true
            binding.btnDeleteProductCategory.isEnabled = binding.etProductCategory.text.isNotEmpty()
        } else {
            binding.tvProductId.text = ""
            binding.etProductName.setText("")
            binding.etProductCategory.setText("")
            binding.etProductSearchTerms.setText("")
            binding.btnDeleteProduct.isEnabled = false
            binding.btnDeleteProductCategory.isEnabled = false
        }
        binding.btnSaveProduct.isEnabled = false
    }

    private fun productFromUi(): Product {
        LOG.d("function: ${this::productFromUi}")
        val id: Long = binding.tvProductId.text.toString().toLongOrNull() ?: 0
        val name: String = binding.etProductName.text.toString()
        val productCategory: Long = binding.etProductCategory.text.toString().toLongOrNull() ?: 0
        val searchTerms: String = binding.etProductSearchTerms.text.toString()
        return Product(id,name,searchTerms,productCategory)
    }

    private fun isProductValid(product: Product?): Boolean{
        LOG.d("function: ${this::isProductValid}")
        if (product != null){
            if (product.name != ""){
                return true
            }
        }
        return false
    }

}