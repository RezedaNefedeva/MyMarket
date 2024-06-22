package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.AddMinusReportActivity.Companion.end_period
import com.rezeda.marketprogram.AddMinusReportActivity.Companion.start_period
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.OrderItemAdapter
import com.rezeda.marketprogram.models.OrderItem
import java.util.ArrayList

class AllPeriodReportActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_period_report)

        var order_all_report_list = findViewById<RecyclerView>(R.id.order_all_report_list)
        var back_from_all_period = findViewById<ImageView>(R.id.back_from_all_period)

        var start_period_text = findViewById<TextView>(R.id.start_period)
        var end_period_text = findViewById<TextView>(R.id.end_period)

        var total_cash_all = 0f
        var total_card_all = 0f
        var total_day = 0f
        var total_dolgi = 0f

        var total_cash_summa = findViewById<TextView>(R.id.total_cash_summa)
        var total_card_summa = findViewById<TextView>(R.id.total_card_summa)
        var total_day_summa = findViewById<TextView>(R.id.total_day_summa)
        var total_dolgi_summa = findViewById<TextView>(R.id.total_dolgi_summa)

        var orderList = JSONHelper.importFromJSONOrder(this)
        var dateOrderList = ArrayList<OrderItem>()

        start_period_text.text = start_period
        end_period_text.text = end_period

        if(orderList!=null){
            for(el in orderList){
                when(el.order_date){
                    in start_period .. end_period ->
                    dateOrderList.add(el)
                }
            }
            order_all_report_list.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            var orderItemAdapter = OrderItemAdapter(dateOrderList, this)
            order_all_report_list.adapter = orderItemAdapter

        } else {
            Toast.makeText(this, "Нет данных для отчета", Toast.LENGTH_LONG).show()
        }


        for(el in dateOrderList){
            when (el.order_cash_card){
                "Наличные" -> total_cash_all += el.order_total
                "Карта" -> total_card_all += el.order_total
                "Долг нал" -> total_cash_all += el.order_total
                "Долг карта" -> total_card_all += el.order_total
            }
            total_day += el.order_total
        }




        total_card_summa.text = total_card_all.toString()
        total_cash_summa.text = total_cash_all.toString()
        total_day_summa.text = total_day.toString()
        total_dolgi_summa.text = total_dolgi.toString()

        back_from_all_period.setOnClickListener { startActivity(Intent(this, ReportsActivity::class.java)) }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ReportsActivity::class.java))
        super.onBackPressed()
    }
}