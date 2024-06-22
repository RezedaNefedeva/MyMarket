package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.SaleItemAdapter
import com.rezeda.marketprogram.models.DolgUsers
import com.rezeda.marketprogram.models.OrderItem
import com.rezeda.marketprogram.models.SaleItem
import java.text.SimpleDateFormat
import java.util.Date

class SalePage : AppCompatActivity() {

    companion object{
        var total_price = 0.0f
        lateinit var total: TextView
    }

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_page)

        var product_name = findViewById<TextView>(R.id.product_name)
        var product_price = findViewById<TextView>(R.id.product_price)
        var edit_amount = findViewById<EditText>(R.id.amount)
        var sale_list = findViewById<RecyclerView>(R.id.sale_list)
        var saleList = mutableListOf<SaleItem>()
        var saleItemAdapter: SaleItemAdapter

        var cartList = JSONHelper.importFromJSON(this)

        var btn_add_product = findViewById<Button>(R.id.btn_add_product)
        var add_amount = findViewById<Button>(R.id.add_amount)
        var btn_cash = findViewById<Button>(R.id.btn_cash)
        var btn_card = findViewById<Button>(R.id.btn_card)
        var btn_dolg = findViewById<Button>(R.id.btn_dolg)
        total = findViewById(R.id.total)
        var back_from_sale = findViewById<ImageView>(R.id.back_from_sale)
        var amount_text = 0.0f
        var summa = 0.0f
        var sale_info = ""
        total_price = 0.0f

        if(cartList!=null){
            saleList.addAll(cartList)
            sale_list.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            saleItemAdapter = SaleItemAdapter(saleList, this)
            sale_list.adapter = saleItemAdapter
            for(el in saleList){
                total_price += el.summa
                sale_info += "\n${el.sale_name}, ${el.amount}, ${el.summa}"
            }
            total.text = total_price.toString()
        } else {
            total_price = 0.0f
            total.text = total_price.toString()
        }
        product_name.text = intent.getStringExtra("product_name")
        product_price.text = intent.getStringExtra("product_price")


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
                amount_text = edit_amount.text.toString().toFloat()
                summa = amount_text * product_price.text.toString().toFloat()
                if(cartList == null){
                    saleList.add(
                        SaleItem(
                            saleList.size + 1,
                            product_name.text.toString(),
                            amount_text,
                            summa
                        )
                    )
                    sale_list.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    saleItemAdapter = SaleItemAdapter(saleList, this)
                    sale_list.adapter = saleItemAdapter
                    cartList = saleList
                } else {
                    saleList=cartList!!
                    saleList.add(SaleItem(
                        saleList.size + 1,
                        product_name.text.toString(),
                        amount_text,
                        summa
                    ))
                    sale_list.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    saleItemAdapter = SaleItemAdapter(saleList, this)
                    sale_list.adapter = saleItemAdapter
                }

                sale_info += "\n${product_name.text}, $amount_text, $summa"

                edit_amount.clearFocus()
                edit_amount.text.clear()
                product_name.text = ""
                product_price.text = ""
                total_price = 0.0f
                for (el in saleList) {
                    total_price += el.summa
                }
                total.text = total_price.toString()

                cartList = saleList
            }
        }

        btn_add_product.setOnClickListener {

            startActivity(Intent(this, SearchActivity::class.java))
            cartList = saleList
            JSONHelper.exportToJSON(this, cartList!!)

        }

        btn_cash.setOnClickListener {

            var produuct_list_JSON = JSONHelper.importFromJSONProductList(this)
            var product_list = produuct_list_JSON

            if (product_list != null) {
                for(el in product_list){
                    for(i in cartList!!){
                        if(el.name == i.sale_name){
                            el.ost -= i.amount
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

            var orderItemList : MutableList<OrderItem>? = JSONHelper.importFromJSONOrder(this)
            var orderItemList2 = mutableListOf<OrderItem>()
            if(orderItemList != null) {
                orderItemList2 = orderItemList
                orderItemList2.add(
                    OrderItem(
                        currentDate,
                        currentTime,
                        sale_info,
                        "Наличные",
                        total_price
                    )
                )
                orderItemList = orderItemList2
                JSONHelper.exportToJSONOrder(this, orderItemList)
            } else {
                orderItemList2.add(
                    OrderItem(
                        currentDate,
                        currentTime,
                        sale_info,
                        "Наличные",
                        total_price
                    )
                )
                JSONHelper.exportToJSONOrder(this, orderItemList2)
            }
            cartList!!.clear()
            JSONHelper.exportToJSON(this, cartList!!)
            Toast.makeText(this, "Оплата наличными ${total.text} руб", Toast.LENGTH_LONG).show()
            var saleList = cartList
            var saleItemAdapter = SaleItemAdapter(saleList, this)
            sale_list.adapter = saleItemAdapter
            total_price = 0.0f
            total.text = total_price.toString()
        }

        btn_card.setOnClickListener {

            var produuct_list_JSON = JSONHelper.importFromJSONProductList(this)
            var product_list = produuct_list_JSON

            if (product_list != null) {
                for(el in product_list){
                    for(i in cartList!!){
                        if(el.name == i.sale_name){
                            el.ost -= i.amount
                        }
                    }
                }
            }

            produuct_list_JSON = product_list
            if (produuct_list_JSON != null) {
                JSONHelper.exportToJSONProductList(this, produuct_list_JSON!!)
            }

            var dateFormat = SimpleDateFormat("d.M.YYYY")
            var currentDate = dateFormat.format(Date())

            var timeFormat = SimpleDateFormat("HH:mm")
            var currentTime = timeFormat.format(Date())

            var orderItemList : MutableList<OrderItem>? = JSONHelper.importFromJSONOrder(this)
            var orderItemList2 = orderItemList
            orderItemList2?.add(OrderItem(currentDate, currentTime, sale_info, "Карта", total_price))
            orderItemList = orderItemList2
            JSONHelper.exportToJSONOrder(this, orderItemList!!)
            cartList!!.clear()
            JSONHelper.exportToJSON(this, cartList!!)
            Toast.makeText(this, "Оплата на карту ${total.text} руб", Toast.LENGTH_LONG).show()
            var saleList = cartList
            var saleItemAdapter = SaleItemAdapter(saleList, this)
            sale_list.adapter = saleItemAdapter
            total_price = 0.0f
            total.text = total_price.toString()
        }

        btn_dolg.setOnClickListener {

            val v = LayoutInflater.from(this).inflate(R.layout.dolg_alert_dialog, null)
            var alert = AlertDialog.Builder(this)
                .setView(v)
                .setTitle("Кому даем в долг?")
                .create()
            var editText = v.findViewById<EditText>(R.id.komu_dolg)
            val btn_cancel_dolg = v.findViewById<Button>(R.id.btn_cancel_dolg)
            val btn_add_dolg = v.findViewById<Button>(R.id.btn_dolg)

                alert.show()

            btn_add_dolg.setOnClickListener {

                var produuct_list_JSON = JSONHelper.importFromJSONProductList(this)
                var product_list = produuct_list_JSON

                if (product_list != null) {
                    for(el in product_list){
                        for(i in cartList!!){
                            if(el.name == i.sale_name){
                                el.ost -= i.amount
                            }
                        }
                    }
                }

                produuct_list_JSON = product_list
                if (produuct_list_JSON != null) {
                    JSONHelper.exportToJSONProductList(this, produuct_list_JSON!!)
                }

                var dolgList: MutableList<DolgUsers>? = JSONHelper.importFromJSONDolg(this)?.toMutableList()

                var dolg_list = mutableListOf<DolgUsers>()
                var isFound = false
                if(dolgList!=null) {
                    dolg_list = dolgList
                    for (el in dolgList) {

                        if (el.nameUser == editText.text.toString()) {
                            isFound = true
                            el.dolgUser = el.dolgUser + " + " + total_price.toString()
                            el.total_dolg_user += total_price
                        }
                    }
                }
                if(!isFound){
                    dolg_list.add(DolgUsers(editText.text.toString(), total_price.toString(), total_price))
                    dolgList = dolg_list

                }

                Toast.makeText(this, "В долг ${editText.text} добавлено ${total.text}", Toast.LENGTH_LONG).show()
                JSONHelper.exportToJSONDolg(this, dolgList!!)


                cartList!!.clear()
                JSONHelper.exportToJSON(this, cartList!!)

                var saleList = cartList
                var saleItemAdapter = SaleItemAdapter(saleList, this)
                sale_list.adapter = saleItemAdapter
                total_price = 0.0f
                total.text = total_price.toString()

                alert.dismiss()
            }

            btn_cancel_dolg.setOnClickListener {
                alert.dismiss()
            }

        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        super.onBackPressed()
    }

}