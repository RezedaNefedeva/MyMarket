package com.rezeda.marketprogram.models

class SaleItem (var sale_id: Int, var sale_name:String, var amount: Float, var summa:Float) {

    override fun toString(): String {
        return "$sale_id $sale_name $amount $summa"
    }
}