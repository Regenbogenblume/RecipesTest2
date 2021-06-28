package com.breev.v2datamodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.breev.v2datamodel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // binding & layout
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UI
        initUI()
    }


    private fun initUI() {
        // Listener
        binding.btnEditProduct.setOnClickListener {
            val intent: Intent = Intent(this, EditProductActivity::class.java)
            startActivity(intent)
        }
        binding.btnEditProductCategory.setOnClickListener {
            val intent: Intent = Intent(this, EditProductCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnViewProducts.setOnClickListener {
            val intent: Intent = Intent(this, ViewProductsActivity::class.java)
            startActivity(intent)
        }
        binding.btnViewProductCategories.setOnClickListener {
            val intent: Intent = Intent(this, ViewProductCategoriesActivity::class.java)
            startActivity(intent)
        }

    }


}