package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.OrderItemAdapter
import com.rezeda.marketprogram.adapters.SaleItemAdapter
import com.rezeda.marketprogram.models.OrderItem
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class PeriodReportActivity : AppCompatActivity() {

    companion object{
        var choose_date: String = ""
        var total_cash = 0f
        var total_card = 0f
        var total_day = 0f
        var total_skidka = 0f
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_report)
        var order_report_list = findViewById<RecyclerView>(R.id.order_report_list)

        var textView_choose_day = findViewById<TextView>(R.id.choose_date)

        var back_from_period = findViewById<ImageView>(R.id.back_from_period)

        var total_cash_summa = findViewById<TextView>(R.id.total_cash_summa)
        var total_card_summa = findViewById<TextView>(R.id.total_card_summa)
        var total_day_summa = findViewById<TextView>(R.id.total_day_summa)
        var skidki = findViewById<TextView>(R.id.skidki)
        var skidkiJSON = JSONHelper.importFromJSONSkidki(this)

        var orderList = JSONHelper.importFromJSONOrder(this)
        var dateOrderList = ArrayList<OrderItem>()

        textView_choose_day.text= choose_date




        if(orderList!=null){
            for(el in orderList){
                if(el.order_date == textView_choose_day.text){
                    dateOrderList.add(el)
                }
            }
            order_report_list.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            var orderItemAdapter = OrderItemAdapter(dateOrderList, this)
            order_report_list.adapter = orderItemAdapter

        } else {
            Toast.makeText(this, "Нет данных для отчета", Toast.LENGTH_LONG).show()
        }

        total_cash = 0f
        total_card = 0f
        total_day = 0f

        for(el in dateOrderList){

            when (el.order_cash_card){
                "Наличные" -> total_cash += el.order_total
                "Карта" -> total_card += el.order_total
                "Долг нал" -> total_cash += el.order_total
                "Долг карта" -> total_card += el.order_total
            }
            total_day += el.order_total
        }



        skidki.text = "Скидок на сумму ${skidkiJSON}"
        total_card_summa.text = total_card.toString()
        total_cash_summa.text = total_cash.toString()
        total_day_summa.text = total_day.toString()

        back_from_period.setOnClickListener { startActivity(Intent(this, ReportsActivity::class.java)) }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, ReportsActivity::class.java))
        super.onBackPressed()
    }
}