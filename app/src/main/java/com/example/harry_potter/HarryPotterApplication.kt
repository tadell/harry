package com.example.harry_potter

import android.app.Application
import com.example.harry_potter.db.ProductRoomDatabase
import com.example.harry_potter.ui.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HarryPotterApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { ProductRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {ProductRepository(database.productDao()) }

}