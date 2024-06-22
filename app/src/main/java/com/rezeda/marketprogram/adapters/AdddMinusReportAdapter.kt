package com.rezeda.marketprogram.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.R
import com.rezeda.marketprogram.models.AddMinusReport
import com.rezeda.marketprogram.models.OrderItem

class AdddMinusReportAdapter (private var addMinusReportList: MutableList<AddMinusReport>?, private var context: Context) :
    RecyclerView.Adapter<AdddMinusReportAdapter.MyViewHolder> (){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val order_time: TextView = view.findViewById(R.id.text_time)
        val order_info: TextView = view.findViewById(R.id.order_info)
        val order_date: TextView = view.findViewById(R.id.text_date)
        val order_add_minus: TextView = view.findViewById(R.id.cash_card)
        val order_total: TextView = view.findViewById(R.id.text_total)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.report_item_design, parent, false)
        return MyViewHolder(view)
    }
    fun setOrderList(addMinusReportList: MutableList<AddMinusReport>){
        this.addMinusReportList = addMinusReportList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return addMinusReportList!!.count()
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.order_info.text = addMinusReportList!![position].order_info
        holder.order_time.text = addMinusReportList!![position].order_time
        holder.order_date.text = addMinusReportList!![position].order_date
        holder.order_add_minus.text = addMinusReportList!![position].order_add_minus
        holder.order_total.text = addMinusReportList!![position].order_total.toString()

    }
}
