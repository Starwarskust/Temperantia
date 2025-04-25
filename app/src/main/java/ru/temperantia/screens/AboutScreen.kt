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
import java.time.LocalDateTime
import kotlin.math.round
import kotlin.random.Random

@Composable
fun AboutScreen() {
    val today = LocalDateTime.now()
    val randSecs = List(10) { Random.nextLong(5 * 86400, 35 * 86400) }.sorted()
    val transactionPresetScope = List(10) { it ->
        Transaction(null, today.minusSeconds(randSecs[it]), 0, Random.nextInt(1, 9), null,
            round(Random.nextDouble(100.0, 1500.0) * 100) / 100, "Comment")
    }
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