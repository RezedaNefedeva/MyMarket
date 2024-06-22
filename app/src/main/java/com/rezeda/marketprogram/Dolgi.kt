package com.rezeda.marketprogram

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.adapters.DolgUserAdapter
import com.rezeda.marketprogram.adapters.JSONHelper
import com.rezeda.marketprogram.adapters.OstatokProductListAdapter
import com.rezeda.marketprogram.models.DolgUsers
import com.rezeda.marketprogram.models.ProductList

class Dolgi : AppCompatActivity() {

    lateinit var dolgi_list_recycler: RecyclerView
    lateinit var dolgiAdapter: DolgUserAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dolgi)

        var dolgi_list_JSON = JSONHelper.importFromJSONDolg(this)

        dolgi_list_recycler = findViewById(R.id.dolgi_list)
        var total_dolgi_summa = findViewById<TextView>(R.id.total_dolgi_summa)
        val btn_back_dolgi = findViewById<ImageView>(R.id.back_from_dolgi)
        var dolgi_summa: Float = 0.0f

         if(dolgi_list_JSON!=null) {
            var dolg_list = dolgi_list_JSON
            dolgiAdapter = DolgUserAdapter(dolg_list, this)
            dolgi_list_recycler.adapter = dolgiAdapter
            for(el in dolg_list){
                dolgi_summa += el.total_dolg_user
            }
        } else {
            Toast.makeText(this, "Долгов нет", Toast.LENGTH_LONG).show()
        }

        total_dolgi_summa.text = dolgi_summa.toString()

        btn_back_dolgi.setOnClickListener { startActivity(Intent(this, ReportsActivity::class.java)) }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, ReportsActivity::class.java))
        super.onBackPressed()
    }
}