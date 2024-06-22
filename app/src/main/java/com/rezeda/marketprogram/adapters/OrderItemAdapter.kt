package com.rezeda.marketprogram.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.PeriodReportActivity.Companion.total_skidka
import com.rezeda.marketprogram.R
import com.rezeda.marketprogram.models.OrderItem
import com.rezeda.marketprogram.models.SaleItem

class OrderItemAdapter (private var orderList: MutableList<OrderItem>?, private var context: Context) :
    RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> (){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val order_time: TextView = view.findViewById(R.id.text_time)
        val order_info: TextView = view.findViewById(R.id.order_info)
        val order_date: TextView = view.findViewById(R.id.text_date)
        val order_cash_card: TextView = view.findViewById(R.id.cash_card)
        val order_total: TextView = view.findViewById(R.id.text_total)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.report_item_design, parent, false)
        return MyViewHolder(view)
    }
    fun setOrderList(orderList: MutableList<OrderItem>){
        this.orderList = orderList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return orderList!!.count()
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.order_info.text = orderList!![position].order_info
        holder.order_time.text = orderList!![position].order_time
        holder.order_date.text = orderList!![position].order_date
        holder.order_cash_card.text = orderList!![position].order_cash_card
        holder.order_total.text = orderList!![position].order_total.toString()
        holder.itemView.setOnClickListener {
            val inflate = LayoutInflater.from(context).inflate(R.layout.minus_edit_amount_alertdialog, null)
            var edit_summa = inflate.findViewById<EditText>(R.id.minus_edit_text)
            val btn_cancel = inflate.findViewById<Button>(R.id.btn_cancel)
            val btn_minus = inflate.findViewById<Button>(R.id.btn_minus)
            btn_minus.text = "Сохранить"
            val alert = AlertDialog.Builder(context)
                .setView(inflate)
                .setTitle("Введите новую сумму")
                .create()
            alert.show()
            btn_cancel.setOnClickListener { alert.dismiss() }
            btn_minus.setOnClickListener {
                var skidkiJSON = JSONHelper.importFromJSONSkidki(context)
                var skidka: Float
                if(skidkiJSON != null) {
                    total_skidka = skidkiJSON.toString().toFloat()
                    total_skidka += orderList!![position].order_total - edit_summa.text.toString()
                        .toFloat()
                } else {
                    total_skidka += orderList!![position].order_total - edit_summa.text.toString()
                        .toFloat()
                }
                skidkiJSON = total_skidka
                JSONHelper.exportToJSONSSkidki(context, skidkiJSON)
                skidka = orderList!![position].order_total - edit_summa.text.toString().toFloat()
                Toast.makeText(context, "Сумма скидки ${skidka}", Toast.LENGTH_LONG).show()
                orderList!![position].order_total = edit_summa.text.toString().toFloat()
                notifyDataSetChanged()




                JSONHelper.exportToJSONOrder(context, orderList!!)
                orderList = JSONHelper.importFromJSONOrder(context)
                alert.dismiss() }
        }

    }
}
