package com.rezeda.marketprogram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.ProductListAdapter
import com.rezeda.marketprogram.models.ProductList

class SearchActivity : AppCompatActivity() {

    lateinit var all_products: RecyclerView
    lateinit var search_product: SearchView
    lateinit var productAdapter: ProductListAdapter
    lateinit var searchList: MutableList<ProductList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        var product_list_JSON = JSONHelper.importFromJSONProductList(this)

        all_products = findViewById(R.id.all_products)
        search_product = findViewById(R.id.search)

        searchList = ArrayList()

        var product_list = product_list_JSON
        var product_list_new = ArrayList<ProductList>()

        if (product_list_JSON!=null) {
            product_list_new = product_list_JSON as ArrayList<ProductList>
            searchList.addAll(product_list_new)

            all_products.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            productAdapter = ProductListAdapter(product_list_new, this)

            all_products.adapter= productAdapter

            search_product.clearFocus()

            search_product.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filteredDataList = product_list_new.filter { item -> item.name.contains(newText!!, true) }
                    productAdapter.setFilteredList(filteredDataList.toMutableList())
                    return true
                }
            })
        } else {

            product_list_JSON = product_list
            if (product_list != null) {
                searchList.addAll(product_list)
            }

            all_products.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            productAdapter = ProductListAdapter(product_list, this)

            all_products.adapter = productAdapter

            search_product.clearFocus()

            search_product.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filteredDataList =
                        product_list?.filter { item -> item.name.contains(newText!!, true) }
                    productAdapter.setFilteredList(filteredDataList!!.toMutableList())
                    return true
                }
            })
        }

        if (product_list_JSON != null) {
            JSONHelper.exportToJSONProductList(this, product_list_JSON)
        }

    }

}