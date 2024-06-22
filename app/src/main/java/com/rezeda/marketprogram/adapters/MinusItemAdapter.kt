package com.rezeda.marketprogram.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.MinusAmount
import com.rezeda.marketprogram.PlusAmount
import com.rezeda.marketprogram.R
import com.rezeda.marketprogram.SalePage
import com.rezeda.marketprogram.models.SaleItem

class MinusItemAdapter (private var saleList: MutableList<SaleItem>?, private var context: Context) :
    RecyclerView.Adapter<MinusItemAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sale_id: TextView = view.findViewById(R.id.sale_id)
        val product_sale_name: TextView = view.findViewById(R.id.product_sale_name)
        val product_amount: TextView = view.findViewById(R.id.product_amount)
        val product_summa: TextView = view.findViewById(R.id.product_summa)
        val sale_delete: ImageView = view.findViewById(R.id.sale_delete)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sale_list_item_design, parent, false)
        return MyViewHolder(view)
    }
    fun setSaleList(saleList: MutableList<SaleItem>){
        this.saleList = saleList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return saleList!!.count()
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.sale_id.text = saleList!![position].sale_id.toString()
        holder.product_sale_name.text = saleList!![position].sale_name
        holder.product_amount.text = saleList!![position].amount.toString()
        holder.product_summa.text = saleList!![position].summa.toString()
        holder.sale_delete.setOnClickListener {
            val v = LayoutInflater.from(context)
            AlertDialog.Builder(context)
                .setTitle("Вы уверены, что хотите удалить позицию ${holder.product_sale_name.text}?")
                .setPositiveButton("Удалить"){
                        dialog,_ ->

                    if(saleList!!.size == 0){
                        MinusAmount.total_price_minus = 0.0f
                        MinusAmount.total.text = SalePage.total_price.toString()
                    } else {
                        MinusAmount.total_price_minus -= saleList!![position].summa
                        MinusAmount.total.text = SalePage.total_price.toString()
                    }
                    saleList?.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    JSONHelper.exportToJSON(context, saleList!!)
                    saleList = JSONHelper.importFromJSON(context)

                    dialog.dismiss()
                }
                .setNegativeButton("Отмена"){
                        dialog,_ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

}