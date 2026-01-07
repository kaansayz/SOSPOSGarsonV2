package com.sospos.garson.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sospos.garson.R
import com.sospos.garson.model.Order

class OrderAdapter(
    private var orders: List<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }
    
    override fun getItemCount(): Int = orders.size
    
    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
    
    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        
        fun bind(order: Order) {
            order.menuItem?.let { menuItem ->
                tvItemName.text = menuItem.name
                
                tvQuantity.text = itemView.context.getString(
                    R.string.item_quantity,
                    order.quantity
                )
                
                val totalPrice = menuItem.price * order.quantity
                tvPrice.text = itemView.context.getString(
                    R.string.item_price,
                    totalPrice
                )
                
                val statusText = when (order.status) {
                    "pending" -> itemView.context.getString(R.string.order_status_pending)
                    "preparing" -> itemView.context.getString(R.string.order_status_preparing)
                    "ready" -> itemView.context.getString(R.string.order_status_ready)
                    "delivered" -> itemView.context.getString(R.string.order_status_delivered)
                    else -> order.status
                }
                tvStatus.text = statusText
            }
        }
    }
}
