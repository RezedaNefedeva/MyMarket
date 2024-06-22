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
import com.rezeda.marketprogram.adapters.AdddMinusReportAdapter
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.OrderItemAdapter
import com.rezeda.marketprogram.models.AddMinusReport
import com.rezeda.marketprogram.models.OrderItem
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class AddMinusReportActivity : AppCompatActivity() {

        companion object{
            lateinit var start_period: String
            lateinit var end_period: String

            var order_date: String? = null
            var order_time: String? = null
            var order_info: String? = null
            var order_add_minus: String? = null
            var order_amount: Float = 0f

        }

        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_add_minus_report)

            var add_minus_report_list = findViewById<RecyclerView>(R.id.add_minus_report_list)
            var back_from_add_minus = findViewById<ImageView>(R.id.back_from_add_minus)

            var start_period_text = findViewById<TextView>(R.id.start_period)
            var end_period_text = findViewById<TextView>(R.id.end_period)

            var total_add = 0f
            var total_minus = 0f

            var total_add_summa = findViewById<TextView>(R.id.total_add)
            var total_minus_summa = findViewById<TextView>(R.id.total_minus)

            var addMinusReport = JSONHelper.importFromJSONAddMinus(this)
            var dateAddMinus = ArrayList<AddMinusReport>()

            start_period_text.text = start_period


            if(addMinusReport!=null){
                for(el in addMinusReport){
                    if(el.order_date == start_period){
                            dateAddMinus.add(el)
                    }
                }
                add_minus_report_list.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                var addMinusAdapter = AdddMinusReportAdapter(dateAddMinus, this)
                add_minus_report_list.adapter = addMinusAdapter


            } else {
                Toast.makeText(this, "Нет данных для отчета", Toast.LENGTH_LONG).show()
            }

            for(el in dateAddMinus){
                when (el.order_add_minus){
                    "Приход" -> total_add += el.order_total
                    "Списание" -> total_minus += el.order_total
                }
            }

            total_add_summa.text = total_add.toString()
            total_minus_summa.text = total_minus.toString()



            back_from_add_minus.setOnClickListener { startActivity(Intent(this, ReportsActivity::class.java)) }
        }

    override fun onBackPressed() {
        startActivity(Intent(this, ReportsActivity::class.java))
        super.onBackPressed()
    }

}