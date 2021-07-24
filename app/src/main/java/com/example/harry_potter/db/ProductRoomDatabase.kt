package com.example.harry_potter.db

import android.content.Context
import androidx.annotation.RawRes
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.harry_potter.R
import com.example.harry_potter.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.reflect.Type
import java.util.*


@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductRoomDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ProductRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ProductRoomDatabase {


            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductRoomDatabase::class.java,
                    "word_database"
                ).addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.productDao())
                }
            }
        }

        suspend fun populateDatabase(productDao: ProductDao) {

            productDao.deleteAll()
//            val productsString = readStringFromFile("products.json")

            var productList: ArrayList<Product> = ArrayList()
            val type: Type = object : TypeToken<List<Product?>?>() {}.type
            productList.addAll(Gson().fromJson(readRawResource(R.raw.product), type))

            for (i in 0 until productList.size) {
                val item = productList[i]
                productDao.insert(item as Product)
          }
        }

       fun readRawResource(@RawRes res: Int): String? {

            val inputStream: InputStream? =
                this::class.java.getResourceAsStream("/assets/products.json")
            return readStream(inputStream)

        }

        private fun readStream(`is`: InputStream?): String? {
            // http://stackoverflow.com/a/5445161
            val s: Scanner = Scanner(`is`).useDelimiter("\\A")
            return if (s.hasNext()) s.next() else ""
        }
    }


}