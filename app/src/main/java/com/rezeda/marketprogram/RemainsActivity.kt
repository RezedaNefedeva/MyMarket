package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.OstatokProductListAdapter
import com.rezeda.marketprogram.models.ProductList

class RemainsActivity : AppCompatActivity() {

    lateinit var all_products: RecyclerView
    lateinit var search_product: SearchView
    lateinit var productAdapter: OstatokProductListAdapter
    lateinit var searchList: ArrayList<ProductList>

    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remains)

        var product_list_JSON = JSONHelper.importFromJSONProductList(this)

        val btn_add_position = findViewById<Button>(R.id.btn_add_position)


        all_products = findViewById(R.id.ostatok)
        search_product = findViewById(R.id.search_ostatok)
        var product_list : MutableList<ProductList>? = null
        if(product_list_JSON == null){
            Toast.makeText(this, "Здесь пока ничего нет", Toast.LENGTH_LONG).show()
        } else {
            product_list = product_list_JSON
            searchList = ArrayList()
            searchList.addAll(product_list!!)
            all_products.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            productAdapter = OstatokProductListAdapter(product_list, this)

            all_products.adapter= productAdapter

            search_product.clearFocus()

            search_product.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filteredDataList = product_list!!.filter { item -> item.name.contains(newText!!, true) }
                    productAdapter.setFilteredList(filteredDataList.toMutableList())
                    return true
                }
            })
        }

        btn_add_position.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("Введите информацию о товаре")
                .create()
            val inflater = LayoutInflater.from(this)
            val dialogLayoutInflater = inflater.inflate(R.layout.add_position_dialog, null)
            val editName = dialogLayoutInflater.findViewById<EditText>(R.id.add_edit_name)
            val editPrice = dialogLayoutInflater.findViewById<EditText>(R.id.add_edit_price)
            val editAddAmount = dialogLayoutInflater.findViewById<EditText>(R.id.add_edit_amount)

            val btn_cancel = dialogLayoutInflater.findViewById<Button>(R.id.btn_add_cancel)
            val btn_add = dialogLayoutInflater.findViewById<Button>(R.id.btn_add)

            builder.setView(dialogLayoutInflater)

            builder.show()

            btn_add.setOnClickListener {
                if(editName.text.isEmpty()){
                    editName.setHintTextColor(Color.RED)
                    Toast.makeText(this, "Введите название товара", Toast.LENGTH_LONG).show()

                } else if (editPrice.text.isEmpty()){
                    editPrice.setHintTextColor(Color.RED)
                    Toast.makeText(this, "Введите цену товара", Toast.LENGTH_LONG).show()


                }else if (editAddAmount.text.isEmpty()){
                    editAddAmount.setHintTextColor(Color.RED)
                    Toast.makeText(this, "Введите остаток товара", Toast.LENGTH_LONG).show()

                } else {
                    product_list?.add(
                        ProductList(
                            product_list?.size!! + 1,
                            editName.text.toString(),
                            editPrice.text.toString(),
                            editAddAmount.text.toString().toFloat()
                        )
                    )
                    builder.dismiss()
                    Toast.makeText(this, "Товар был добавлен", Toast.LENGTH_LONG).show()
                    JSONHelper.exportToJSONProductList(this, product_list!!)
                    product_list = JSONHelper.importFromJSONProductList(this) as ArrayList<ProductList>
                    all_products.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                    productAdapter = OstatokProductListAdapter(product_list, this)

                    all_products.adapter = productAdapter
                }
            }

            btn_cancel.setOnClickListener {builder.dismiss() }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, ReportsActivity::class.java))
        super.onBackPressed()
    }
}