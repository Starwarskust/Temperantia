package ru.temperantia.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

//@Entity(tableName = "categories")
//data class Category(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int?,
//    val name: String
//)

//@Entity(tableName = "accounts")
//data class Account(
//    val id: Int,
//    val name: String,
//    val date: Date
//)

data class CategoryIcon(
    val id: Int,
    val name: String,
    val totalExpense: Double,
    val icon: ImageVector,
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