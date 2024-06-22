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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rezeda.marketprogram.R
import com.rezeda.marketprogram.models.ProductList

class OstatokProductListAdapter (private var products: MutableList<ProductList>?, private var context: Context) :
    RecyclerView.Adapter<OstatokProductListAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val product_id: TextView = view.findViewById(R.id.product_id)
        val product_name: TextView = view.findViewById(R.id.product_name)
        val product_price: TextView = view.findViewById(R.id.product_price)
        val product_ost: TextView = view.findViewById(R.id.product_ost)
        val delete_product: ImageView = view.findViewById(R.id.delete)
        val edit_product: ImageView = view.findViewById(R.id.edit_img)
    }

    fun setFilteredList(products: MutableList<ProductList>){
        this.products = products
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ostatok_report_design, parent, false)
        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return products!!.count()
    }

    @SuppressLint("DiscouragedApi", "MissingInflatedId")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.product_id.text = products!![position].id.toString()
        holder.product_name.text = products!![position].name
        holder.product_price.text = products!![position].price
        holder.product_ost.text = products!![position].ost.toString()
        holder.delete_product.setOnClickListener {
            val passAlert = LayoutInflater.from(context).inflate(R.layout.pass_alert, null)
            val btn_cancel_pass = passAlert.findViewById<Button>(R.id.btn_cancel_pass)
            val btn_ok_pass = passAlert.findViewById<Button>(R.id.btn_ok_pass)
            var edit_pass = passAlert.findViewById<EditText>(R.id.pass)
            val alert_pass = AlertDialog.Builder(context)
                .setTitle("Для продолжения введите пароль")
                .setView(passAlert)
                .create()

            alert_pass.show()
            btn_cancel_pass.setOnClickListener { alert_pass.dismiss() }
            btn_ok_pass.setOnClickListener {
                if(edit_pass.text.toString() == "0215"){

                    val v = LayoutInflater.from(context)
                    AlertDialog.Builder(context)
                        .setTitle("Вы уверены, что хотите удалить позицию ${holder.product_name.text}?")
                        .setPositiveButton("Удалить"){
                                dialog,_ ->
                            products?.removeAt(holder.adapterPosition)
                            notifyItemRemoved(holder.adapterPosition)
                            JSONHelper.exportToJSONProductList(context, products!!)
                            products = JSONHelper.importFromJSONProductList(context)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Отмена"){
                                dialog,_ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                    alert_pass.dismiss()
                } else {
                    alert_pass.setTitle("Пароль не верный")
                    edit_pass.setText("")
                    edit_pass.setHintTextColor(Color.RED)
                    edit_pass.setHint("Пароль не верный")
                }
            }

        }
        holder.edit_product.setOnClickListener {
            val passAlert = LayoutInflater.from(context).inflate(R.layout.pass_alert, null)
            val btn_cancel_pass = passAlert.findViewById<Button>(R.id.btn_cancel_pass)
            val btn_ok_pass = passAlert.findViewById<Button>(R.id.btn_ok_pass)
            var edit_pass = passAlert.findViewById<EditText>(R.id.pass)
            val alert_pass = AlertDialog.Builder(context)
                .setTitle("Для продолжения введите пароль")
                .setView(passAlert)
                .create()
            alert_pass.show()
            btn_cancel_pass.setOnClickListener { alert_pass.dismiss() }
            btn_ok_pass.setOnClickListener {
                if (edit_pass.text.toString() == "0215") {

                    val builder = AlertDialog.Builder(context)
                        .setTitle("Введите информацию о товаре")
                        .create()
                    val inflater = LayoutInflater.from(context)
                    val dialogLayoutInflater = inflater.inflate(R.layout.add_position_dialog, null)
                    val editName = dialogLayoutInflater.findViewById<EditText>(R.id.add_edit_name)
                    val editPrice = dialogLayoutInflater.findViewById<EditText>(R.id.add_edit_price)
                    val editAddAmount =
                        dialogLayoutInflater.findViewById<EditText>(R.id.add_edit_amount)

                    val btn_cancel = dialogLayoutInflater.findViewById<Button>(R.id.btn_add_cancel)
                    val btn_edit = dialogLayoutInflater.findViewById<Button>(R.id.btn_add)
                    btn_edit.text = "Изменить"
                    editName.setText(products!![position].name)
                    editPrice.setText(products!![position].price)
                    editAddAmount.setText(products!![position].ost.toString())

                    builder.setView(dialogLayoutInflater)

                    builder.show()

                    btn_cancel.setOnClickListener {builder.dismiss() }

                    btn_edit.setOnClickListener {
                        products!![position].name = editName.text.toString()
                        products!![position].price = editPrice.text.toString()
                        products!![position].ost = editAddAmount.text.toString().toFloat()
                        notifyDataSetChanged()


                        builder.dismiss()
                        Toast.makeText(context, "Товар был изменен", Toast.LENGTH_LONG).show()
                        JSONHelper.exportToJSONProductList(context, products!!)
                        products = JSONHelper.importFromJSONProductList(context)


                    }
                    alert_pass.dismiss()
                } else {
                    alert_pass.setTitle("Пароль не верный")
                    edit_pass.setText("")
                    edit_pass.setHintTextColor(Color.RED)
                    edit_pass.setHint("Пароль не верный")
                }
            }


        }
    }

}