package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.errorprone.annotations.Var
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rezeda.marketprogram.AddMinusReportActivity.Companion.end_period
import com.rezeda.marketprogram.AddMinusReportActivity.Companion.start_period
import com.rezeda.marketprogram.PeriodReportActivity.Companion.total_card
import com.rezeda.marketprogram.PeriodReportActivity.Companion.total_cash
import com.rezeda.marketprogram.PeriodReportActivity.Companion.total_day
import com.rezeda.marketprogram.PeriodReportActivity.Companion.total_skidka
import com.rezeda.marketprogram.adapters.JSONHelper
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ReportsActivity : AppCompatActivity() {
//    companion object{
//        lateinit var choose_period_text: String
//    }


    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        val database = FirebaseDatabase.getInstance("https://my-market-usadba-myasnika-default-rtdb.europe-west1.firebasedatabase.app")
        val tableSaleItem = database.getReference("SaleItem")
        val tableOrderItem = database.getReference("OrderItem")
        val tableProductList = database.getReference("ProductList")
        val tableAddMinusReport = database.getReference("AddMinusReport")
        val tableDolgUsers = database.getReference("DolgUsers")

        var date = findViewById<TextView>(R.id.date)
        var btn_period_report = findViewById<Button>(R.id.btn_period_report)
        var btn_remains = findViewById<Button>(R.id.btn_remains)
        var btn_all_period = findViewById<Button>(R.id.btn_all_period)
        var back_from_reoprt = findViewById<ImageView>(R.id.back_from_reports)
        val btn_add_minus_report = findViewById<Button>(R.id.btn_add_minus)
        val btn_dolgi = findViewById<Button>(R.id.btn_dolg_report)
        val edit_report = findViewById<TextView>(R.id.edit_reports)


        var dateFormat = SimpleDateFormat("dd-MM-YYYY")
        var currentDate = dateFormat.format(Date())
        date.text=currentDate

        btn_period_report.setOnClickListener {

            val inflater = LayoutInflater.from(this).inflate(R.layout.activity_calendar, null)
            val calendarView = inflater.findViewById<CalendarView>(R.id.calendarView)

            val alertCalendar = AlertDialog.Builder(this)
                .setView(inflater)
                .setTitle("Выберите дату для отчета")
                .create()
            alertCalendar.show()

            calendarView.setOnDateChangeListener { view, year, month, day ->
                PeriodReportActivity.choose_date = "$day.${month + 1}.$year"
                startActivity(Intent(this, PeriodReportActivity::class.java))
            }

        }


        btn_remains.setOnClickListener { startActivity(Intent(this, RemainsActivity::class.java)) }
        back_from_reoprt.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

        btn_all_period.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTitleText("Выберите период для отчета")
                .setPositiveButtonText("Выбрать")
                .setNegativeButtonText("Отмена")
                .build()

            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener { datePicked ->
                var startDate = datePicked.first
                var seconddate = datePicked.second

                start_period = convertDate(startDate)
                end_period = convertDate(seconddate)

                startActivity(Intent(this, AllPeriodReportActivity::class.java))
            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
            }

        }

        btn_add_minus_report.setOnClickListener {

            val inflater = LayoutInflater.from(this).inflate(R.layout.activity_calendar, null)
            val calendarView = inflater.findViewById<CalendarView>(R.id.calendarView)

            val alertCalendar = AlertDialog.Builder(this)
                .setView(inflater)
                .setTitle("Выберите дату для отчета")
                .create()
            alertCalendar.show()

            calendarView.setOnDateChangeListener { view, year, month, day ->
                AddMinusReportActivity.start_period = "$day.${month + 1}.$year"
                startActivity(Intent(this, AddMinusReportActivity::class.java))
            }

        }

        btn_dolgi.setOnClickListener { startActivity(Intent(this, Dolgi::class.java)) }

        edit_report.setOnClickListener {
            val passAlert = LayoutInflater.from(this).inflate(R.layout.pass_alert, null)
            val btn_cancel_pass = passAlert.findViewById<Button>(R.id.btn_cancel_pass)
            val btn_ok_pass = passAlert.findViewById<Button>(R.id.btn_ok_pass)
            var edit_pass = passAlert.findViewById<EditText>(R.id.pass)
            val alert_pass = AlertDialog.Builder(this)
                .setTitle("Для продолжения введите пароль")
                .setView(passAlert)
                .create()

            alert_pass.show()
            btn_cancel_pass.setOnClickListener { alert_pass.dismiss() }
            btn_ok_pass.setOnClickListener {
                if (edit_pass.text.toString() == "0215") {

                    val builder = AlertDialog.Builder(this)
                        .setTitle("Вы уверены, что хотите очистить отчеты? Отчет за день, отчет по периодам и скидки обнулятся")
                        .setPositiveButton("Очистить"){
                            dialog, _ ->
                            tableOrderItem.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val jsonOrder = JSONHelper.importFromJSONOrder(this@ReportsActivity)
                                tableOrderItem.setValue(jsonOrder)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@ReportsActivity, "Для продолжения нужно подключиться к интернету", Toast.LENGTH_LONG).show()
                            }
                        })

                            tableProductList.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val jsonProduct = JSONHelper.importFromJSONProductList(this@ReportsActivity)
                                    tableProductList.setValue(jsonProduct)
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@ReportsActivity, "Для продолжения нужно подключиться к интернету", Toast.LENGTH_LONG).show()
                                }
                            })

                            tableDolgUsers.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val jsonDolg = JSONHelper.importFromJSONDolg(this@ReportsActivity)
                                    tableDolgUsers.setValue(jsonDolg)
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@ReportsActivity, "Для продолжения нужно подключиться к интернету", Toast.LENGTH_LONG).show()
                                }
                            })

                            tableAddMinusReport.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val jsonAddMinus = JSONHelper.importFromJSONAddMinus(this@ReportsActivity)
                                    tableAddMinusReport.setValue(jsonAddMinus)
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@ReportsActivity, "Для продолжения нужно подключиться к интернету", Toast.LENGTH_LONG).show()
                                }
                            })

                            var orderItemList = JSONHelper.importFromJSONOrder(this@ReportsActivity)
                            orderItemList?.clear()
                            JSONHelper.exportToJSONOrder(this@ReportsActivity, orderItemList!!)

                            var addMinusList = JSONHelper.importFromJSONAddMinus(this@ReportsActivity)
                            addMinusList?.clear()
                            JSONHelper.exportToJSONAddMinus(this@ReportsActivity, addMinusList!!)

                            var skidkiJSON = JSONHelper.importFromJSONSkidki(this@ReportsActivity)
                            skidkiJSON = 0.0f
                            JSONHelper.exportToJSONSSkidki(this@ReportsActivity, skidkiJSON)
                            total_cash = 0.0f
                            total_card = 0.0f
                            total_day = 0.0f
                            total_skidka = 0.0f

                            dialog.dismiss()
                        }
                        .setNegativeButton("Отмена"){
                            dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    builder.show()
                    alert_pass.dismiss()
                }
                else {
                    alert_pass.setTitle("Пароль не верный")
                    edit_pass.setText("")
                    edit_pass.setHintTextColor(Color.RED)
                    edit_pass.setHint("Пароль не верный")
                }

            }
        }
    }

    private fun convertDate (date: Long) : String{
        var period_date = Date(date)
        var format = SimpleDateFormat("d.M.YYYY")
        return format.format(period_date)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        super.onBackPressed()
    }
}