package com.example.harry_potter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_table")
class Product(
    @PrimaryKey
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "imageURL")
    val imageUrl: String,
    @ColumnInfo(name = "tag")
    val tag: Boolean = false
)
