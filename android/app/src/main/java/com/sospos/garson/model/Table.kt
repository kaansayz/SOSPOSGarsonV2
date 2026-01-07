package com.sospos.garson.model

import com.google.gson.annotations.SerializedName

data class Table(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("table_number")
    val tableNumber: Int,
    
    @SerializedName("status")
    val status: String = "empty", // empty, occupied
    
    @SerializedName("waiter_id")
    val waiterId: Int? = null,
    
    @SerializedName("orders")
    val orders: List<Order>? = null
)
