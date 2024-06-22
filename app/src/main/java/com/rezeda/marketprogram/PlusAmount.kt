package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.PlusItemAdapter
import com.rezeda.marketprogram.adapters.SaleItemAdapter
import com.rezeda.marketprogram.models.AddMinusReport
import com.rezeda.marketprogram.models.SaleItem
import java.text.SimpleDateFormat
import java.util.Date

class PlusAmount : AppCompatActivity() {

    companion object{
        var total_price_plus = 0.0f
        lateinit var total: TextView
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_amount)

    var product_name = findViewById<TextView>(R.id.product_name)
    var product_price: String? = null
    var edit_amount = findViewById<EditText>(R.id.amount_plus)
    var cartList = JSONHelper.importFromJSON(this)
    var sale_list = findViewById<RecyclerView>(R.id.plus_list)
    var btn_add_product = findViewById<Button>(R.id.btn_add_product)
    var add_amount = findViewById<Button>(R.id.btn_plus_amount)
    var btn_minus = findViewById<Button>(R.id.btn_plus)
    total = findViewById<TextView>(R.id.total_plus)
    var back_from_sale = findViewById<ImageView>(R.id.back_from_plus)

    var amount_text: Float
    var summa: Float
    var sale_info = ""


    if(cartList!=null){
        sale_list.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var saleItemAdapter = PlusItemAdapter(cartList, this)
        sale_list.adapter = saleItemAdapter
        for(el in cartList){
            total_price_plus += el.summa
            sale_info += "\n${el.sale_name}, ${el.amount}, ${el.summa}"
        }
        total.text = total_price_plus.toString()
    }

    product_name.text = intent.getStringExtra("product_name")
        product_price = intent.getStringExtra("product_price")



    back_from_sale.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

    add_amount.setOnClickListener {
        val v: View? = this.currentFocus
        if(v!=null){
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
        if (product_name.text.isEmpty()){
            product_name.setText("Добавьте товар")
            product_name.setTextColor(Color.RED)
        } else if (edit_amount.text.isEmpty()) {
            edit_amount.setHintTextColor(Color.RED)

        } else {
            total_price_plus = 0.0f
            amount_text = edit_amount.text.toString().toFloat()
            summa = amount_text * product_price.toString().toFloat()
            var saleList = cartList

            saleList!!.add(
                SaleItem(
                    saleList.size + 1,
                    product_name.text.toString(),
                    amount_text,
                    summa
                )
            )

            sale_info += "\n${product_name.text}, $amount_text, $summa"

            edit_amount.clearFocus()
            edit_amount.text.clear()
            product_name.text = ""
            for (el in saleList) {
                total_price_plus += el.summa
            }
            total.text = total_price_plus.toString()

            var saleItemAdapter = PlusItemAdapter(saleList, this)
            sale_list.adapter = saleItemAdapter

            cartList = saleList
        }
    }

    btn_add_product.setOnClickListener {
        startActivity(
            Intent(
                this,
                PlusSearchActivity::class.java
            )
        )
        JSONHelper.exportToJSON(this, cartList!!)
    }

    btn_minus.setOnClickListener {
        if(cartList==null){
            if (product_name.text.isEmpty()){
                product_name.setText("Добавьте товар")
                product_name.setTextColor(Color.RED)
            } else if (edit_amount.text.isEmpty()) {
                edit_amount.setHintTextColor(Color.RED)
            }
        } else {
            var produuct_list_JSON = JSONHelper.importFromJSONProductList(this)
            var product_list = produuct_list_JSON

            if (product_list != null) {
                for (el in product_list) {
                    for (i in cartList!!) {
                        if (el.name == i.sale_name) {
                            el.ost += i.amount
                        }
                    }
                }
            }

            produuct_list_JSON = product_list
            if (produuct_list_JSON != null) {
                JSONHelper.exportToJSONProductList(this, produuct_list_JSON)
            }

            var dateFormat = SimpleDateFormat("d.M.YYYY")
            var currentDate = dateFormat.format(Date())

            var timeFormat = SimpleDateFormat("HH:mm")
            var currentTime = timeFormat.format(Date())

            var orderItemList: MutableList<AddMinusReport>? =
                JSONHelper.importFromJSONAddMinus(this)
            var orderItemList2 = mutableListOf<AddMinusReport>()
            if (orderItemList != null) {
                orderItemList2 = orderItemList
                orderItemList2.add(
                    AddMinusReport(
                        currentDate,
                        currentTime,
                        sale_info,
                        "Приход",
                        total_price_plus
                    )
                )
                orderItemList = orderItemList2
                JSONHelper.exportToJSONAddMinus(this, orderItemList)
            } else {
                orderItemList2.add(
                    AddMinusReport(
                        currentDate,
                        currentTime,
                        sale_info,
                        "Приход",
                        total_price_plus
                    )
                )
                JSONHelper.exportToJSONAddMinus(this, orderItemList2)
            }
            cartList!!.clear()
            JSONHelper.exportToJSON(this, cartList!!)
            Toast.makeText(this, "Добавлено", Toast.LENGTH_LONG).show()
            var saleList = cartList
            var saleItemAdapter = PlusItemAdapter(saleList, this)
            sale_list.adapter = saleItemAdapter
            total_price_plus = 0.0f
            total.text = total_price_plus.toString()
        }
    }

}

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        super.onBackPressed()
    }

}
