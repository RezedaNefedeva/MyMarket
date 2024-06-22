package com.rezeda.marketprogram.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.PlusAmount
import com.rezeda.marketprogram.R
import com.rezeda.marketprogram.models.ProductList

class PlusAdapter (private var products: MutableList<ProductList>?, private var context: Context) :
    RecyclerView.Adapter<PlusAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val product_id: TextView = view.findViewById(R.id.product_id)
        val product_name: TextView = view.findViewById(R.id.product_name)
        val product_price: TextView = view.findViewById(R.id.product_price)
        val product_ost: TextView = view.findViewById(R.id.product_ost)
    }

    fun setFilteredList(products: MutableList<ProductList>){
        this.products = products
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return products!!.count()
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.product_id.text = products!![position].id.toString()
        holder.product_name.text = products!![position].name
        holder.product_price.text = products!![position].price
        holder.product_ost.text = products!![position].ost.toString()
//        holder.delete_product.setOnClickListener {
//            val v = LayoutInflater.from(context)
//            AlertDialog.Builder(context)
//                .setTitle("Вы уверены, что хотите удалить позицию ${holder.product_name.text}?")
//                .setPositiveButton("Удалить"){
//                        dialog,_ ->
//                    products?.removeAt(holder.adapterPosition)
//                    notifyItemRemoved(holder.adapterPosition)
//                    dialog.dismiss()
//                }
//                .setNegativeButton("Отмена"){
//                        dialog,_ ->
//                    dialog.dismiss()
//                }
//                .create()
//                .show()
//            notifyDataSetChanged()
//        }
        holder.itemView.setOnClickListener { val intent = Intent(context, PlusAmount::class.java)
            intent.putExtra("product_name", products!![position].name)
            intent.putExtra("product_price", products!![position].price)
            context.startActivity(intent)}
    }

}