package com.sospos.garson.api

import com.sospos.garson.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("api/login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): Response<ApiResponse<User>>
    
    @GET("api/tables")
    suspend fun getTables(): Response<ApiResponse<List<Table>>>
    
    @GET("api/tables/{id}")
    suspend fun getTable(
        @Path("id") tableId: Int
    ): Response<ApiResponse<Table>>
    
    @PUT("api/tables/{id}")
    suspend fun updateTable(
        @Path("id") tableId: Int,
        @Body table: Table
    ): Response<ApiResponse<Table>>
    
    @GET("api/menu")
    suspend fun getMenu(): Response<ApiResponse<List<MenuItem>>>
    
    @GET("api/menu/category/{category}")
    suspend fun getMenuByCategory(
        @Path("category") category: String
    ): Response<ApiResponse<List<MenuItem>>>
    
    @GET("api/orders/table/{tableId}")
    suspend fun getOrdersByTable(
        @Path("tableId") tableId: Int
    ): Response<ApiResponse<List<Order>>>
    
    @POST("api/orders")
    suspend fun createOrder(
        @Body order: Order
    ): Response<ApiResponse<Order>>
    
    @PUT("api/orders/{id}")
    suspend fun updateOrder(
        @Path("id") orderId: Int,
        @Body order: Order
    ): Response<ApiResponse<Order>>
    
    @POST("api/orders/send-to-kitchen")
    suspend fun sendOrdersToKitchen(
        @Body orderIds: Map<String, List<Int>>
    ): Response<ApiResponse<String>>
    
    @DELETE("api/orders/{id}")
    suspend fun deleteOrder(
        @Path("id") orderId: Int
    ): Response<ApiResponse<String>>
}
