package com.sospos.garson.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("password")
    val password: String? = null,
    
    @SerializedName("role")
    val role: String = "waiter",
    
    @SerializedName("token")
    val token: String? = null
)
