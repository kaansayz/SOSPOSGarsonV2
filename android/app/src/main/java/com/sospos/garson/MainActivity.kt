package com.sospos.garson

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sospos.garson.adapter.TableAdapter
import com.sospos.garson.api.RetrofitClient
import com.sospos.garson.model.Table
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var toolbar: Toolbar
    private lateinit var rvTables: RecyclerView
    private lateinit var btnLogout: Button
    private lateinit var tableAdapter: TableAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        toolbar = findViewById(R.id.toolbar)
        rvTables = findViewById(R.id.rvTables)
        btnLogout = findViewById(R.id.btnLogout)
        
        setSupportActionBar(toolbar)
        
        btnLogout.setOnClickListener {
            logout()
        }
        
        setupRecyclerView()
        loadTables()
    }
    
    override fun onResume() {
        super.onResume()
        loadTables()
    }
    
    private fun setupRecyclerView() {
        tableAdapter = TableAdapter(emptyList()) { table ->
            openTable(table)
        }
        rvTables.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = tableAdapter
        }
    }
    
    private fun loadTables() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getTables()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val tables = response.body()?.data ?: emptyList()
                    tableAdapter.updateTables(tables)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_server),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
    
    private fun openTable(table: Table) {
        val intent = Intent(this, TableActivity::class.java)
        intent.putExtra("table_id", table.id)
        intent.putExtra("table_number", table.tableNumber)
        startActivity(intent)
    }
    
    private fun logout() {
        // Clear saved data
        val sharedPref = getSharedPreferences("SOSPOS", MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        
        // Navigate to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
