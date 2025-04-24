package ru.temperantia.data

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val date: Date,
    @ColumnInfo(name = "account_id")
    val accountId: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    val subcategory: String?,
    val amount: Double,
    val comment: String?
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val group: String,
    val name: String,
    val icon: ImageVector,
    val color: Color,
    @ColumnInfo(name = "ignore_in_balance")
    val ignoreInBalance: Boolean
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun intToColor(value: Int) : Color {
        return Color(value)
    }

    @TypeConverter
    fun colorToInt(color: Color) : Int {
        return color.toArgb()
    }

    @TypeConverter
    fun iconByName(name: String): ImageVector {
        val cl = Class.forName("androidx.compose.material.icons.outlined.${name}Kt")
        val method = cl.declaredMethods.first()
        return method.invoke(null, Icons.Outlined) as ImageVector
    }

    @TypeConverter
    fun getIconName(icon: ImageVector): String {
        return icon.name.split(".")[1]
    }
}