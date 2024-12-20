package ru.temperantia.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val date: Date,
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

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}