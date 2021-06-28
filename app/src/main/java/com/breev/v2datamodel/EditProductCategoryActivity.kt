package com.breev.v2datamodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.data.viewmodels.RecipeViewModel
import com.breev.v2datamodel.databinding.ActivityEditProductCategoryBinding
import com.breev.v2datamodel.utils.EasyLog
import com.breev.v2datamodel.utils.InjectorUtils

class EditProductCategoryActivity : AppCompatActivity() {

    companion object{
        const val INTENT_EXTRA_PRODUCT_CATEGORY = "intent_extra_product_category"
    }

    private val LOG = EasyLog(javaClass.name)
    private lateinit var binding: ActivityEditProductCategoryBinding
    private lateinit var viewModel: RecipeViewModel
    private var currentProductCategory: ProductCategory? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        LOG.d("function: onCreate")
        // view binding and Layout
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // view model
        viewModel = InjectorUtils.provideRecipeViewModel(application,this)
        initLiveData()

        // UI
        initUI()
    }


    // ---------------------------------------------------------------------
    // USER INTERFACE ------------------------------------------------------
    // ---------------------------------------------------------------------
    private fun initUI(){
        LOG.d("function: ${this::initUI}")
        getIntentData()
        productCategoryToUi(currentProductCategory)

        // Listener
        binding.btnSaveProductCategory.setOnClickListener { saveProductCategory() }
        binding.btnDeleteProductCategory.setOnClickListener { deleteProductCategory() }

        binding.etProductCategoryName.addTextChangedListener ( textWatcher )
        binding.etProductCategoryOrder.addTextChangedListener ( textWatcher )
    }

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) { enableDisableSaveBtn() }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
    }

    private fun enableDisableSaveBtn(){
        LOG.d("function: ${this::enableDisableSaveBtn}")
        // enable the Save Button only of the fields in the UI differ from the entry in the database
        val uiProductCategory = productCategoryFromUi()
        binding.btnSaveProductCategory.isEnabled = (uiProductCategory != currentProductCategory && isProductCategoryValid(uiProductCategory))
    }


    // ---------------------------------------------------------------------
    // ACTIVITY'S PURPOSE FUNCTIONS ----------------------------------------
    // ---------------------------------------------------------------------
    private fun saveProductCategory() {
        LOG.d("function: ${this::saveProductCategory}")
        val productCategory = productCategoryFromUi()
        if (isProductCategoryValid(productCategory)) {
            viewModel.writeProductCategoryToDatabase(productCategory)
        } else {
            LOG.d("Product Category not valid")
        }
        finish()
    }

    private fun deleteProductCategory() {
        LOG.d("function: ${this::deleteProductCategory}")
        if (currentProductCategory != null) {
            viewModel.deleteProductCategoryFromDatabase(currentProductCategory!!)
        } else {
            LOG.d("nothing to delete")
        }
        finish()
    }

    private fun initLiveData(){
        LOG.d("function: ${this::initLiveData}")
        // observe LiveData for Toasting message
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
        if (intent.hasExtra(INTENT_EXTRA_PRODUCT_CATEGORY)) {
            val productCategory: ProductCategory =
                intent.extras?.get(INTENT_EXTRA_PRODUCT_CATEGORY) as ProductCategory
            currentProductCategory = productCategory
        }
        LOG.d("currentProductCategory: $currentProductCategory")
    }

    private fun productCategoryToUi(productCategory: ProductCategory?){
        LOG.d("function: ${this::productCategoryToUi}")
        if (productCategory!=null) {
            binding.tvProductCategoryId.text = productCategory.id.toString()
            binding.etProductCategoryName.setText(productCategory.name)
            binding.etProductCategoryOrder.setText(productCategory.order.toString())
            binding.btnDeleteProductCategory.isEnabled = true
        } else {
            binding.tvProductCategoryId.text = ""
            binding.etProductCategoryName.setText("")
            binding.etProductCategoryOrder.setText("")
            binding.btnDeleteProductCategory.isEnabled = false
        }
        binding.btnSaveProductCategory.isEnabled = false
    }

    private fun productCategoryFromUi(): ProductCategory {
        LOG.d("function: ${this::productCategoryFromUi}")
        val id: Long = binding.tvProductCategoryId.text.toString().toLongOrNull() ?: 0
        val name: String = binding.etProductCategoryName.text.toString()
        val order: Int = binding.etProductCategoryOrder.text.toString().toIntOrNull() ?: 0
        return ProductCategory(id,name,order)
    }

    private fun isProductCategoryValid(productCategory: ProductCategory?): Boolean{
        LOG.d("function: ${this::isProductCategoryValid}")
        if (productCategory != null){
            if (productCategory.name != ""){
                return true
            }
        }
        return false
    }


}