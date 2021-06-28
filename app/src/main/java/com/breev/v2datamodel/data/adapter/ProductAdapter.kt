package com.breev.v2datamodel.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.databinding.RecyclerRowProductBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var products = emptyList<Product>()
    var onItemClick: ((Int, Product) -> Unit)? = null
    var selectionTracker: SelectionTracker<Long>? = null

    fun setItems(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerRowProductBinding) : RecyclerView.ViewHolder(binding.root)  {

        init {
            itemView.setOnClickListener{ onItemClick?.invoke(adapterPosition, products[adapterPosition]) }
        }

        fun bind(product: Product, position: Int, isActivated: Boolean = false){
            binding.tvProductId.text= product.id.toString()
            binding.tvProductName.text = product.name
            binding.tvProductCategoryId.text = product.productCategoryId.toString()
            binding.tvProductSearchTerms.text = product.searchTerms
            itemView.isActivated = isActivated
        }

        // for Selection Tracker
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = adapterPosition
            override fun getSelectionKey(): Long = itemId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerRowProductBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = products[position]
        if (selectionTracker != null) {
            holder.bind(item, position, selectionTracker!!.isSelected(position.toLong()))
        } else {
            holder.bind(item, position)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }



    // for Selection Tracker
    init {
        setHasStableIds(true)
    }
    override fun getItemId(position: Int): Long {
        //return products[position].id
        return position.toLong()
    }
    //fun getItemPosition(id: Long) = products.indexOfFirst { it.id == id }

    fun getItemAtAdapterPosition(position: Long): Product {
        return products[position.toInt()]
    }
}