package ru.temperantia.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val date: Int,
    val account: String,
    val category: String,
    val subcategory: String?,
    val amount: Double,
    val comment: String?
)

//data class Account(
//    val id: Int,
//    val date: Date,
//    val account: String,
//    val category: String,
//    val subcategory: String,
//    val amount: Float,
//    val comment: String
//)

data class Category(
    val id: Int,
    val name: String,
    val totalExpense: Float,
    val color: Color
)