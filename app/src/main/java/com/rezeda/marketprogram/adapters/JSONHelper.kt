package com.rezeda.marketprogram.adapters

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rezeda.marketprogram.models.AddMinusReport
import com.rezeda.marketprogram.models.DolgUsers
import com.rezeda.marketprogram.models.OrderItem
import com.rezeda.marketprogram.models.ProductList
import com.rezeda.marketprogram.models.SaleItem
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class JSONHelper {

    companion object{
        private val fileName1 = "sale.json"
        private val fileNameOrder = "order.json"
        private var fileProductList = "product.json"
        private val fileAddMinus = "add-minus-report.json"
        private val fileDolg = "dolg.json"
        private val fileSkidki = "skidki.json"


        fun exportToJSON (context: Context, saleList: MutableList<SaleItem>){
            val jsonString = Gson().toJson(saleList)
            val file: FileOutputStream? = context.openFileOutput(fileName1, Context.MODE_PRIVATE)
            file?.write(jsonString.toByteArray())
            file?.close()
        }

        fun importFromJSON(context: Context) : MutableList<SaleItem>? {
            val file = File(context.filesDir, fileName1)
            if(!file.exists()) return null

            val fileInputStream: FileInputStream? = context.openFileInput(fileName1)
            val stream = InputStreamReader(fileInputStream)
            val listType = object : TypeToken<MutableList<SaleItem>>() {}.type
            val dataItems: MutableList<SaleItem> = Gson().fromJson(stream, listType)
            stream.close()
            fileInputStream?.close()
            return dataItems
        }

        fun exportToJSONOrder (context: Context, orderList: MutableList<OrderItem>){
            val jsonString = Gson().toJson(orderList)
            val file: FileOutputStream? = context.openFileOutput(fileNameOrder, Context.MODE_PRIVATE)
            file?.write(jsonString.toByteArray())
            file?.close()
        }

        fun importFromJSONOrder(context: Context) : MutableList<OrderItem>? {
            val file = File(context.filesDir, fileNameOrder)
            if(!file.exists()) return null

            val fileInputStream: FileInputStream? = context.openFileInput(fileNameOrder)
            val stream = InputStreamReader(fileInputStream)
            val listType = object : TypeToken<MutableList<OrderItem>>() {}.type
            val dataItems: MutableList<OrderItem> = Gson().fromJson(stream, listType)
            stream.close()
            fileInputStream?.close()
            return dataItems
        }

        fun exportToJSONProductList (context: Context, productList: MutableList<ProductList>?){
            val jsonString = Gson().toJson(productList)
            val file: FileOutputStream? = context.openFileOutput(fileProductList, Context.MODE_PRIVATE)
            file?.write(jsonString.toByteArray())
            file?.close()
        }

        fun importFromJSONProductList(context: Context) : MutableList<ProductList>? {
            val file = File(context.filesDir, fileProductList)
            if(!file.exists()) return null

            val fileInputStream: FileInputStream? = context.openFileInput(fileProductList)
            val stream = InputStreamReader(fileInputStream)
            val listType = object : TypeToken<MutableList<ProductList>>() {}.type
            val dataItems: MutableList<ProductList> = Gson().fromJson(stream, listType)
            stream.close()
            fileInputStream?.close()
            return dataItems
        }

        fun exportToJSONAddMinus (context: Context, productList:  MutableList<AddMinusReport>){
            val jsonString = Gson().toJson(productList)
            val file: FileOutputStream? = context.openFileOutput(fileAddMinus, Context.MODE_PRIVATE)
            file?.write(jsonString.toByteArray())
            file?.close()
        }

        fun importFromJSONAddMinus(context: Context) : MutableList<AddMinusReport>? {
            val file = File(context.filesDir, fileAddMinus)
            if(!file.exists()) return null

            val fileInputStream: FileInputStream? = context.openFileInput(fileAddMinus)
            val stream = InputStreamReader(fileInputStream)
            val listType = object : TypeToken<MutableList<AddMinusReport>>() {}.type
            val dataItems: MutableList<AddMinusReport> = Gson().fromJson(stream, listType)
            stream.close()
            fileInputStream?.close()
            return dataItems
        }

        fun exportToJSONDolg (context: Context, dolgList:  MutableList<DolgUsers>){
            val jsonString = Gson().toJson(dolgList)
            val file: FileOutputStream? = context.openFileOutput(fileDolg, Context.MODE_PRIVATE)
            file?.write(jsonString.toByteArray())
            file?.close()
        }

        fun importFromJSONDolg(context: Context) : MutableList<DolgUsers>? {
            val file = File(context.filesDir, fileDolg)
            if(!file.exists()) return null

            val fileInputStream: FileInputStream? = context.openFileInput(fileDolg)
            val stream = InputStreamReader(fileInputStream)
            val listType = object : TypeToken<MutableList<DolgUsers>>() {}.type
            val dataItems: MutableList<DolgUsers> = Gson().fromJson(stream, listType)
            stream.close()
            fileInputStream?.close()
            return dataItems
        }

        fun exportToJSONSSkidki (context: Context, skidki:  Float){
            val jsonString = Gson().toJson(skidki)
            val file: FileOutputStream? = context.openFileOutput(fileSkidki, Context.MODE_PRIVATE)
            file?.write(jsonString.toByteArray())
            file?.close()
        }

        fun importFromJSONSkidki(context: Context) : Float? {
            val file = File(context.filesDir, fileSkidki)
            if(!file.exists()) return null

            val fileInputStream: FileInputStream? = context.openFileInput(fileSkidki)
            val stream = InputStreamReader(fileInputStream)
            val listType = object : TypeToken<Float>() {}.type
            val dataItems: Float = Gson().fromJson(stream, listType)
            stream.close()
            fileInputStream?.close()
            return dataItems
        }
    }
}