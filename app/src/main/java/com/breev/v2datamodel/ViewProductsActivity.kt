package com.breev.v2datamodel


import MyItemDetailsLookup
import RecyclerViewIdKeyProvider
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breev.v2datamodel.data.datamodels.Product
import com.breev.v2datamodel.data.adapter.ProductAdapter
import com.breev.v2datamodel.data.viewmodels.RecipeViewModel
import com.breev.v2datamodel.databinding.ActivityViewProductsBinding
import com.breev.v2datamodel.utils.EasyLog
import com.breev.v2datamodel.utils.InjectorUtils


class ViewProductsActivity : AppCompatActivity(), ActionMode.Callback {

    private val LOG = EasyLog(javaClass.name)
    private lateinit var binding: ActivityViewProductsBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var products: LiveData<List<Product>>
    private var selectedItems: MutableList<Long> = mutableListOf()
    private var selectedProducts: ArrayList<Product> = arrayListOf()
    private var actionMode: ActionMode? = null

    companion object {
        private const val RV_SELECTION_TRACKER = "SELECTION TRACKER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LOG.d("function: onCreate")

        // view binding & layout
        super.onCreate(savedInstanceState)
        binding = ActivityViewProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // View Model
        viewModel = InjectorUtils.provideRecipeViewModel(application, this)

        // UI
        initRecyclerView()

        // Toolbar
        initToolbar()

    }


    // ---------------------------------------------------------------------
    // RECYCLER VIEW & LIVE DATA -------------------------------------------
    // ---------------------------------------------------------------------
    private fun initRecyclerView(){
        LOG.d("function: ${this::initRecyclerView}")
        recyclerView = binding.rvProducts
        // Layout Manager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Adapter
        adapter = ProductAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, layoutManager.orientation)
        )
        // Data
        initRecyclerViewLiveData()
        // onClick Listener
        adapter.onItemClick = this::onRecyclerItemClick
        // Selection Tracker
        val selectionTracker = SelectionTracker.Builder<Long>(
            RV_SELECTION_TRACKER,
            recyclerView,
            RecyclerViewIdKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        adapter.selectionTracker = selectionTracker
        // Observer for Selection Tracker to enter Action Mode
        selectionTracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    selectionTracker.let {
                        selectedItems = it.selection.toMutableList()
                        selectedProducts.clear()
                        selectedItems.forEach { item ->
                            selectedProducts.add(adapter.getItemAtAdapterPosition(item))
                        }
                        LOG.d("selected Items: $selectedItems $selectedProducts")
                        if (selectedItems.isEmpty()) {
                            actionMode?.finish()
                        } else {
                            if (actionMode == null) actionMode = startSupportActionMode(this@ViewProductsActivity)
                            actionMode?.title = "${selectedItems.size}"
                        }
                    }
                }
            })


    }

    private fun initRecyclerViewLiveData(){
        LOG.d("function: ${this::initRecyclerViewLiveData}")
        products = viewModel.getProducts()
        products.observe(this, { items: List<Product> ->
            adapter.setItems(items)
            recyclerView.smoothScrollToPosition(items.size)
        })
    }

    private fun onRecyclerItemClick(position: Int, product: Product) {
        LOG.d("function: ${this::onRecyclerItemClick}: $position, $product")
        val intent: Intent = Intent(this, EditProductActivity::class.java)
        intent.putExtra(EditProductActivity.INTENT_EXTRA_PRODUCT, product)
        startActivity(intent)
    }

    private fun filterProducts(filter:String){
        LOG.d("function: ${this::filterProducts}: $filter")
        viewModel.setProductFilter(filter.toLowerCase())
    }


    // ---------------------------------------------------------------------
    // OPTIONS MENU --------------------------------------------------------
    // ---------------------------------------------------------------------
    private lateinit var optionsMenu: Menu

    private fun initToolbar(){
        LOG.d("function: ${this::initToolbar}")
        val toolbar: Toolbar = binding.topAppBar as Toolbar
        setSupportActionBar(toolbar)
        val topAppBar = supportActionBar

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    Toast.makeText(this, "Search", Toast.LENGTH_LONG).show()
                    false
                }
                else -> false
            }
        }

        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Navigation", Toast.LENGTH_LONG).show()
            startActivity(parentActivityIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        LOG.d("function: ${this::onCreateOptionsMenu}")
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        optionsMenu = menu

        // Define the search widget behaviour
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                LOG.d("function: ${this::onMenuItemActionExpand}")
                for (i in 0 until (menu.size())) {
                    val menuItem = menu.getItem(i)
                    // menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER) // show all menu icons in overflow menu
                    menuItem.isVisible = false // don't even show overflow menu -> Search widget is full width
                }
                return true // Return true to expand action view
            }
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                LOG.d("function: ${this::onMenuItemActionCollapse}")
                invalidateOptionsMenu() // reset all changes by calling onCreateOptionsMenu again
                return true // Return true to collapse action view
            }
        })

        val searchView = searchItem?.actionView as SearchView
        //searchView.queryHint = "Search Products ...";

        // define Listener to handle text input in search widget
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                LOG.d("function: ${this::onQueryTextSubmit}: $query")
                return false // Return false to hide soft keyboard
            }
            override fun onQueryTextChange(newText: String): Boolean {
                LOG.d("function: ${this::onQueryTextChange}: $newText")
                filterProducts(newText)
                return true
            }
        })
        return true // options menu created
    }

//     // now using toolbar.setOnMenuItemClickListener instead
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId){
//            R.id.delete -> {
//                //Toast.makeText(this, "Delete", Toast.LENGTH_LONG).show()
//                true
//            }
//            else -> false //super.onOptionsItemSelected(item)
//        }
//    }

    // ---------------------------------------------------------------------
    // ACTION MODE ---------------------------------------------------------
    // ---------------------------------------------------------------------
    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.action_mode_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_view_delete -> {
                Toast.makeText(this, selectedItems.toString(), Toast.LENGTH_LONG).show()
            }
        }
        //mode.finish() // exit Action-Mode
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        adapter.selectionTracker?.clearSelection()
        actionMode = null
    }




}