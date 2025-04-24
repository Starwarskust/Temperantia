package ru.temperantia.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Transaction
import java.util.Date

@Composable
fun AboutScreen() {
    val transactionPresetScope = listOf(
        Transaction(null, Date(2024 - 1900, 11, 15), 0, 4, "Подкатегория", 124.67, "Пятерочка"),
        Transaction(null, Date(2024 - 1900, 11, 16), 0, 1, "Подкатегория", 345.17, null),
        Transaction(null, Date(2024 - 1900, 11, 16), 0, 4, "Подкатегория", 1023.0,   "Перекресток"),
        Transaction(null, Date(2024 - 1900, 11, 18), 0, 2, null,           350.10, "Горздрав"),
        Transaction(null, Date(2024 - 1900, 11, 19), 0, 3, "Подкатегория", 345.17, null)
    )
    Column {
        Text("AboutScreen")

        val transactionDao = AppDatabase.instance.transactionDao()
        val transactionList = transactionDao.getAll()
        var n by remember { mutableIntStateOf(transactionList.size) }
        OutlinedButton(
            onClick = {
                transactionDao.insertAll(transactionPresetScope)
                n += transactionPresetScope.size
            }
        ) {
            Text("Push ${transactionPresetScope.size} transactions")
        }
        OutlinedButton(
            onClick = {
                transactionDao.deleteAll()
                n = 0
            }
        ) {
            Text("Clean database")
        }
        Text("Now DB has $n records")
    }
}