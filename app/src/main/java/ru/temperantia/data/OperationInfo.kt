package ru.temperantia.data

import androidx.compose.ui.graphics.Color

data class OperationRecord(
    val id: Int,
    val category: String,
    val comment: String,
    val cost: Float
)

//data class OperationRecord(
//    val id: Int,
//    val date: Date,
//    val category: String,
//    val value: Float,
//    val comment: String
//)

data class Category(
    val categoryId: Int,
    val categoryName: String,
    val totalExpense: Float,
    val color: Color
)