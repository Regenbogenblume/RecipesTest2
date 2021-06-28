package com.breev.v2datamodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.data.adapter.ProductCategoryAdapter
import com.breev.v2datamodel.data.viewmodels.RecipeViewModel
import com.breev.v2datamodel.databinding.ActivityViewProductCategoriesBinding
import com.breev.v2datamodel.utils.InjectorUtils

class ViewProductCategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewProductCategoriesBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var adapter: ProductCategoryAdapter
    private lateinit var productCategories: LiveData<List<ProductCategory>>

    override fun onCreate(savedInstanceState: Bundle?) {

        // view binding & layout
        super.onCreate(savedInstanceState)
        binding = ActivityViewProductCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // View Model
        viewModel = InjectorUtils.provideRecipeViewModel(application,this)

        // UI
        initRecyclerView()
    }


    // ---------------------------------------------------------------------
    // RECYCLER VIEW & LIVE DATA -------------------------------------------
    // ---------------------------------------------------------------------
    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvProductCategories.layoutManager = layoutManager
        adapter = ProductCategoryAdapter()
        initRecyclerViewLiveData()
        adapter.onItemClick = this::onRecyclerItemClick
        binding.rvProductCategories.adapter = adapter
        binding.rvProductCategories.addItemDecoration(
            DividerItemDecoration(this, layoutManager.orientation)
        )
    }

    private fun initRecyclerViewLiveData(){
        productCategories = viewModel.getAllProductCategories()
        productCategories.observe(this, { items: List<ProductCategory> ->
            adapter.setItems(items)
            binding.rvProductCategories.smoothScrollToPosition(items.size)
        })
    }

    private fun onRecyclerItemClick(position: Int, productCategory: ProductCategory) {
        val intent: Intent = Intent(this, EditProductCategoryActivity::class.java)
        intent.putExtra(EditProductCategoryActivity.INTENT_EXTRA_PRODUCT_CATEGORY, productCategory)
        startActivity(intent)
    }


}