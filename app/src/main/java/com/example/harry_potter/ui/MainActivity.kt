package com.example.harry_potter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.harry_potter.R
import com.example.harry_potter.viewmodel.ProductViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harry_potter.HarryPotterApplication
import com.example.harry_potter.adapter.ProductListAdapter
import com.example.harry_potter.viewmodel.ProductViewModelFactory

class MainActivity : AppCompatActivity() {
    private val productViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((application as HarryPotterApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ProductListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        productViewModel.allProducts.observe(this, Observer { products ->
            // Update the cached copy of the words in the adapter.
            products?.let { adapter.submitList(it) }
        })
    }
}