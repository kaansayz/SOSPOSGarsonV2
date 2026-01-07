package com.sospos.garson.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sospos.garson.R
import com.sospos.garson.model.MenuItem

class MenuAdapter(
    private var menuItems: List<MenuItem>,
    private val onAddToOrder: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.bind(menuItem)
    }
    
    override fun getItemCount(): Int = menuItems.size
    
    fun updateMenu(newItems: List<MenuItem>) {
        menuItems = newItems
        notifyDataSetChanged()
    }
    
    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMenuItemName: TextView = itemView.findViewById(R.id.tvMenuItemName)
        private val tvMenuItemDescription: TextView = itemView.findViewById(R.id.tvMenuItemDescription)
        private val tvMenuItemPrice: TextView = itemView.findViewById(R.id.tvMenuItemPrice)
        private val btnAddToOrder: Button = itemView.findViewById(R.id.btnAddToOrder)
        
        fun bind(menuItem: MenuItem) {
            tvMenuItemName.text = menuItem.name
            tvMenuItemDescription.text = menuItem.description
            tvMenuItemPrice.text = itemView.context.getString(
                R.string.price,
                menuItem.price
            )
            
            btnAddToOrder.isEnabled = menuItem.available
            btnAddToOrder.setOnClickListener {
                onAddToOrder(menuItem)
            }
        }
    }
}
