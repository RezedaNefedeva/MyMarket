package com.rezeda.marketprogram.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.R
import com.rezeda.marketprogram.models.DolgUsers
import com.rezeda.marketprogram.models.OrderItem
import java.text.SimpleDateFormat
import java.util.Date

class DolgUserAdapter (private var dolgList: MutableList<DolgUsers>?, private var context: Context) :
    RecyclerView.Adapter<DolgUserAdapter.MyViewHolder> (){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name_user = view.findViewById<TextView>(R.id.name_user)
        var dolg_user = view.findViewById<TextView>(R.id.dolg_user)
        var total_dolg = view.findViewById<TextView>(R.id.total_dolg_user)
        val btn_otdali = view.findViewById<Button>(R.id.btn_otdali)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dolg_list_item, parent, false)
        return MyViewHolder(view)
    }
    fun setOrderList(dolgList: MutableList<DolgUsers>?){
        this.dolgList = dolgList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return dolgList!!.count()
    }

    @SuppressLint("DiscouragedApi", "MissingInflatedId")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name_user.text = dolgList!![position].nameUser
        holder.dolg_user.text = dolgList!![position].dolgUser
        holder.total_dolg.text = dolgList!![position].total_dolg_user.toString()
        holder.btn_otdali.setOnClickListener {
            val inflater = LayoutInflater.from(context).inflate(R.layout.dolg_otdali, null)
            val howMuch = inflater.findViewById<EditText>(R.id.howMuch)
            val btn_dolg_cancel = inflater.findViewById<Button>(R.id.btn_dolg_cancel)
            val btn_dolg_minus_card = inflater.findViewById<Button>(R.id.btn_dolg_minus_card)
            val btn_dolg_minus_cash = inflater.findViewById<Button>(R.id.btn_dolg_minus_cash)
            val alert = AlertDialog.Builder(context)
                .setView(inflater)
                .setTitle("Введите сумму долга, который отдали")
                .create()

            alert.show()

            btn_dolg_cancel.setOnClickListener {
                alert.dismiss()
            }

            btn_dolg_minus_card.setOnClickListener {
                if(howMuch.text.isEmpty()){
                    howMuch.setHintTextColor(Color.RED)
                } else if (howMuch.text.toString().toFloat() == dolgList!![position].total_dolg_user){
                    Toast.makeText(context, "Вернули долг на карту", Toast.LENGTH_LONG).show()
                    var dateFormat = SimpleDateFormat("d.M.YYYY")
                    var currentDate = dateFormat.format(Date())

                    var timeFormat = SimpleDateFormat("HH:mm")
                    var currentTime = timeFormat.format(Date())

                    var orderItemList : MutableList<OrderItem>? = JSONHelper.importFromJSONOrder(context)
                    var orderItemList2 = mutableListOf<OrderItem>()
                    if(orderItemList != null) {
                        orderItemList2 = orderItemList
                        orderItemList2.add((OrderItem(currentDate, currentTime, dolgList!![position].nameUser, "Долг карта", howMuch.text.toString().toFloat())))
                        orderItemList = orderItemList2
                        JSONHelper.exportToJSONOrder(context, orderItemList)
                    } else {
                        orderItemList2.add(
                            OrderItem(
                                currentDate,
                                currentTime,
                                dolgList!![position].nameUser,
                                "Долг карта",
                                howMuch.text.toString().toFloat()
                            )
                        )
                        JSONHelper.exportToJSONOrder(context, orderItemList2)
                    }
//                    total_card += howMuch.text.toString().toFloat()
                    dolgList?.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    JSONHelper.exportToJSONDolg(context, dolgList!!)
                    alert.dismiss()
                } else {
                    Toast.makeText(context, "Вернули часть долга на карту", Toast.LENGTH_LONG).show()
                    var dateFormat = SimpleDateFormat("d.M.YYYY")
                    var currentDate = dateFormat.format(Date())

                    var timeFormat = SimpleDateFormat("HH:mm")
                    var currentTime = timeFormat.format(Date())

                    var orderItemList : MutableList<OrderItem>? = JSONHelper.importFromJSONOrder(context)
                    var orderItemList2 = orderItemList
                    orderItemList2?.add(OrderItem(currentDate, currentTime, dolgList!![position].nameUser, "Долг карта", howMuch.text.toString().toFloat()))
                    orderItemList = orderItemList2
                    JSONHelper.exportToJSONOrder(context, orderItemList!!)
//                    total_card += howMuch.text.toString().toFloat()
                    dolgList!![position].dolgUser = dolgList!![position].dolgUser + " - " + howMuch.text.toString()
                    dolgList!![position].total_dolg_user = dolgList!![position].total_dolg_user - howMuch.text.toString().toFloat()
                    notifyDataSetChanged()
                    JSONHelper.exportToJSONDolg(context, dolgList!!)
                    alert.dismiss()
                }

            }

            btn_dolg_minus_cash.setOnClickListener {
                if(howMuch.text.isEmpty()){
                    howMuch.setHintTextColor(Color.RED)
                } else if(howMuch.text.toString().toFloat() == dolgList!![position].total_dolg_user){
                    Toast.makeText(context, "Вернули весь долг наличными", Toast.LENGTH_LONG).show()
                    var dateFormat = SimpleDateFormat("d.M.YYYY")
                    var currentDate = dateFormat.format(Date())

                    var timeFormat = SimpleDateFormat("HH:mm")
                    var currentTime = timeFormat.format(Date())

                    var orderItemList : MutableList<OrderItem>? = JSONHelper.importFromJSONOrder(context)
                    var orderItemList2 = mutableListOf<OrderItem>()
                    if(orderItemList != null) {
                        orderItemList2 = orderItemList as MutableList<OrderItem>
                        orderItemList2.add((OrderItem(currentDate, currentTime, dolgList!![position].nameUser, "Долг нал", howMuch.text.toString().toFloat())))
                        orderItemList = orderItemList2
                        JSONHelper.exportToJSONOrder(context, orderItemList!!)
                    } else {
                        orderItemList2.add(
                            OrderItem(
                                currentDate,
                                currentTime,
                                dolgList!![position].nameUser,
                                "Долг нал",
                                howMuch.text.toString().toFloat()
                            )
                        )
                        JSONHelper.exportToJSONOrder(context, orderItemList2)
                    }
//                    total_cash += howMuch.text.toString().toFloat()
                    dolgList?.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    JSONHelper.exportToJSONDolg(context, dolgList!!)
                    alert.dismiss()
                } else {
                    Toast.makeText(context, "Вернули часть долга наличными", Toast.LENGTH_LONG).show()
                    var dateFormat = SimpleDateFormat("d.M.YYYY")
                    var currentDate = dateFormat.format(Date())

                    var timeFormat = SimpleDateFormat("HH:mm")
                    var currentTime = timeFormat.format(Date())

                    var orderItemList : MutableList<OrderItem>? = JSONHelper.importFromJSONOrder(context)
                    var orderItemList2 = orderItemList
                    orderItemList2?.add(OrderItem(currentDate, currentTime, dolgList!![position].nameUser, "Долг нал", howMuch.text.toString().toFloat()))
                    orderItemList = orderItemList2
                    JSONHelper.exportToJSONOrder(context, orderItemList!!)
//                    total_cash += howMuch.text.toString().toFloat()
                    dolgList!![position].dolgUser = dolgList!![position].dolgUser + " - " + howMuch.text.toString()
                    dolgList!![position].total_dolg_user = dolgList!![position].total_dolg_user - howMuch.text.toString().toFloat()
                    notifyDataSetChanged()
                    JSONHelper.exportToJSONDolg(context, dolgList!!)
                    alert.dismiss()
                }

            }
        }

    }
}