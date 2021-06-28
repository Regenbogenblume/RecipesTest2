package com.breev.v2datamodel.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breev.v2datamodel.data.datamodels.ProductCategory
import com.breev.v2datamodel.databinding.RecyclerRowProductCategoryBinding

class ProductCategoryAdapter() : RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {

    private var productCategories = emptyList<ProductCategory>()
    var onItemClick: ((Int, ProductCategory) -> Unit)? = null

    fun setItems(productCategories: List<ProductCategory>){
        this.productCategories = productCategories
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerRowProductCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener{ onItemClick?.invoke(adapterPosition, productCategories[adapterPosition]) }
        }

        fun bind(productCategory: ProductCategory, position: Int){
            binding.tvProductCategoryId.text= productCategory.id.toString()
            binding.tvProductCategoryName.text = productCategory.name
            binding.tvProductCategoryOrder.text = productCategory.order.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerRowProductCategoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productCategories[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return productCategories.size
    }
}