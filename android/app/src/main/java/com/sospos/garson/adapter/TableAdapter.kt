package com.sospos.garson.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sospos.garson.R
import com.sospos.garson.model.Table

class TableAdapter(
    private var tables: List<Table>,
    private val onTableClick: (Table) -> Unit
) : RecyclerView.Adapter<TableAdapter.TableViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table, parent, false)
        return TableViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val table = tables[position]
        holder.bind(table)
    }
    
    override fun getItemCount(): Int = tables.size
    
    fun updateTables(newTables: List<Table>) {
        tables = newTables
        notifyDataSetChanged()
    }
    
    inner class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTableNumber: TextView = itemView.findViewById(R.id.tvTableNumber)
        private val tvTableStatus: TextView = itemView.findViewById(R.id.tvTableStatus)
        private val cardTable: CardView = itemView.findViewById(R.id.cardTable)
        
        fun bind(table: Table) {
            tvTableNumber.text = itemView.context.getString(
                R.string.table_title,
                table.tableNumber
            )
            
            val statusText = if (table.status == "empty") {
                itemView.context.getString(R.string.table_empty)
            } else {
                itemView.context.getString(R.string.table_occupied)
            }
            tvTableStatus.text = statusText
            
            val backgroundColor = if (table.status == "empty") {
                ContextCompat.getColor(itemView.context, R.color.table_empty)
            } else {
                ContextCompat.getColor(itemView.context, R.color.table_occupied)
            }
            cardTable.setCardBackgroundColor(backgroundColor)
            
            itemView.setOnClickListener {
                onTableClick(table)
            }
        }
    }
}
