package com.sospos.garson.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("table_id")
    val tableId: Int,
    
    @SerializedName("menu_item_id")
    val menuItemId: Int,
    
    @SerializedName("menu_item")
    val menuItem: MenuItem? = null,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("status")
    val status: String = "pending", // pending, preparing, ready, delivered
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("notes")
    val notes: String? = null
)
