package com.sospos.garson

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sospos.garson.adapter.OrderAdapter
import com.sospos.garson.api.RetrofitClient
import com.sospos.garson.model.Order
import kotlinx.coroutines.launch

class TableActivity : AppCompatActivity() {
    
    private lateinit var toolbar: Toolbar
    private lateinit var rvOrders: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnAddOrder: Button
    private lateinit var btnSendToKitchen: Button
    private lateinit var btnCloseTable: Button
    
    private lateinit var orderAdapter: OrderAdapter
    private var tableId: Int = 0
    private var tableNumber: Int = 0
    private var orders: List<Order> = emptyList()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)
        
        tableId = intent.getIntExtra("table_id", 0)
        tableNumber = intent.getIntExtra("table_number", 0)
        
        toolbar = findViewById(R.id.toolbar)
        rvOrders = findViewById(R.id.rvOrders)
        tvTotal = findViewById(R.id.tvTotal)
        btnAddOrder = findViewById(R.id.btnAddOrder)
        btnSendToKitchen = findViewById(R.id.btnSendToKitchen)
        btnCloseTable = findViewById(R.id.btnCloseTable)
        
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.table_title, tableNumber)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            finish()
        }
        
        setupRecyclerView()
        setupButtons()
        loadOrders()
    }
    
    override fun onResume() {
        super.onResume()
        loadOrders()
    }
    
    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(emptyList())
        rvOrders.apply {
            layoutManager = LinearLayoutManager(this@TableActivity)
            adapter = orderAdapter
        }
    }
    
    private fun setupButtons() {
        btnAddOrder.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("table_id", tableId)
            startActivity(intent)
        }
        
        btnSendToKitchen.setOnClickListener {
            showSendToKitchenDialog()
        }
        
        btnCloseTable.setOnClickListener {
            showCloseTableDialog()
        }
    }
    
    private fun loadOrders() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getOrdersByTable(tableId)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    orders = response.body()?.data ?: emptyList()
                    orderAdapter.updateOrders(orders)
                    updateTotal()
                } else {
                    Toast.makeText(
                        this@TableActivity,
                        getString(R.string.error_server),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@TableActivity,
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
    
    private fun updateTotal() {
        var total = 0.0
        orders.forEach { order ->
            order.menuItem?.let { menuItem ->
                total += menuItem.price * order.quantity
            }
        }
        tvTotal.text = getString(R.string.total, total)
    }
    
    private fun showSendToKitchenDialog() {
        val pendingOrders = orders.filter { it.status == "pending" }
        
        if (pendingOrders.isEmpty()) {
            Toast.makeText(this, "Gönderilecek sipariş yok", Toast.LENGTH_SHORT).show()
            return
        }
        
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirm_send_order))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                sendToKitchen(pendingOrders)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    
    private fun sendToKitchen(pendingOrders: List<Order>) {
        lifecycleScope.launch {
            try {
                val orderIds = pendingOrders.mapNotNull { it.id }
                val response = RetrofitClient.apiService.sendOrdersToKitchen(
                    mapOf("order_ids" to orderIds)
                )
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        this@TableActivity,
                        getString(R.string.success_order_sent),
                        Toast.LENGTH_SHORT
                    ).show()
                    loadOrders()
                } else {
                    Toast.makeText(
                        this@TableActivity,
                        getString(R.string.error_server),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@TableActivity,
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
    
    private fun showCloseTableDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirm_close_table))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                closeTable()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    
    private fun closeTable() {
        lifecycleScope.launch {
            try {
                val table = com.sospos.garson.model.Table(
                    id = tableId,
                    tableNumber = tableNumber,
                    status = "empty"
                )
                
                val response = RetrofitClient.apiService.updateTable(tableId, table)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        this@TableActivity,
                        getString(R.string.success_table_closed),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@TableActivity,
                        getString(R.string.error_server),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@TableActivity,
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
}
