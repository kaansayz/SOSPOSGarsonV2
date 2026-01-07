package com.sospos.garson

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.sospos.garson.adapter.MenuAdapter
import com.sospos.garson.api.RetrofitClient
import com.sospos.garson.model.MenuItem
import com.sospos.garson.model.Order
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {
    
    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var rvMenu: RecyclerView
    
    private lateinit var menuAdapter: MenuAdapter
    private var tableId: Int = 0
    private var allMenuItems: List<MenuItem> = emptyList()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        
        tableId = intent.getIntExtra("table_id", 0)
        
        toolbar = findViewById(R.id.toolbar)
        tabLayout = findViewById(R.id.tabLayout)
        rvMenu = findViewById(R.id.rvMenu)
        
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            finish()
        }
        
        setupTabs()
        setupRecyclerView()
        loadMenu()
    }
    
    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.category_all)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.category_food)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.category_drink)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.category_dessert)))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filterMenu(tab?.position ?: 0)
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupRecyclerView() {
        menuAdapter = MenuAdapter(emptyList()) { menuItem ->
            showAddOrderDialog(menuItem)
        }
        rvMenu.apply {
            layoutManager = LinearLayoutManager(this@MenuActivity)
            adapter = menuAdapter
        }
    }
    
    private fun loadMenu() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getMenu()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    allMenuItems = response.body()?.data ?: emptyList()
                    menuAdapter.updateMenu(allMenuItems)
                } else {
                    Toast.makeText(
                        this@MenuActivity,
                        getString(R.string.error_server),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MenuActivity,
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
    
    private fun filterMenu(position: Int) {
        val filteredItems = when (position) {
            0 -> allMenuItems // All
            1 -> allMenuItems.filter { it.category == "food" }
            2 -> allMenuItems.filter { it.category == "drink" }
            3 -> allMenuItems.filter { it.category == "dessert" }
            else -> allMenuItems
        }
        menuAdapter.updateMenu(filteredItems)
    }
    
    private fun showAddOrderDialog(menuItem: MenuItem) {
        val quantities = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        
        AlertDialog.Builder(this)
            .setTitle(menuItem.name)
            .setItems(quantities) { _, which ->
                val quantity = which + 1
                addOrder(menuItem, quantity)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    
    private fun addOrder(menuItem: MenuItem, quantity: Int) {
        lifecycleScope.launch {
            try {
                val order = Order(
                    tableId = tableId,
                    menuItemId = menuItem.id,
                    quantity = quantity,
                    status = "pending"
                )
                
                val response = RetrofitClient.apiService.createOrder(order)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        this@MenuActivity,
                        "Sipariş eklendi",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@MenuActivity,
                        getString(R.string.error_server),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MenuActivity,
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
}
