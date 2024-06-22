package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.models.ProductList
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SimpleDateFormat", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sale = findViewById<Button>(R.id.sale)
        val report = findViewById<Button>(R.id.reports)
        val minus = findViewById<Button>(R.id.minus)
        val plus = findViewById<Button>(R.id.plus)
        var date = findViewById<TextView>(R.id.date)


        var dateFormat = SimpleDateFormat("dd-MM-YYYY")
        var currentDate = dateFormat.format(Date())
        date.text=currentDate


        sale.setOnClickListener { startActivity(Intent(this, SalePage::class.java)) }

        report.setOnClickListener { startActivity(Intent(this, ReportsActivity::class.java)) }

        minus.setOnClickListener { startActivity(Intent(this, MinusAmount::class.java)) }

        plus.setOnClickListener { startActivity(Intent(this, PlusAmount::class.java)) }

    }

    override fun onBackPressed() {
        val alert_exit = AlertDialog.Builder(this)
            .setTitle("Вы хотите выйти из приложения?")
            .setPositiveButton("Да"){
                dialog, _ ->
                super.onBackPressed()
                finishAffinity()
            }
            .setNegativeButton("Нет"){
                dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alert_exit.show()

    }
}