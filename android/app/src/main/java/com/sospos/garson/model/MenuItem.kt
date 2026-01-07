package com.sospos.garson.model

import com.google.gson.annotations.SerializedName

data class MenuItem(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("category")
    val category: String, // food, drink, dessert
    
    @SerializedName("available")
    val available: Boolean = true
)
