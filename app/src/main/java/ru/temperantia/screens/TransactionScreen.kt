package ru.temperantia.screens

import android.icu.text.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Transaction
import ru.temperantia.ui.theme.yellowButton
import java.util.Date

@Composable
fun TransactionScreen(onNavigateToInputScreen: () -> Unit) {
    val transactionDao = AppDatabase.instance.transactionDao()
    val transactionList = transactionDao.getAll()
    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn (
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = transactionList.sortedByDescending { it.date },
                key = { item -> item.id!! }
            ) { transactionItem ->
                TransactionBlock(transactionItem)
            }
        }
        FloatingActionButton(
            onClick = onNavigateToInputScreen,
            containerColor = yellowButton,
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionCardPreview() {
    val previewTransaction = Transaction(
        id = null,
        date = Date(),
        account = "Карта",
        categoryId = 0, // TODO this id may crush visualization
        subcategory = null,
        amount = 123.45,
        comment = "Комментарий"
    )
    TransactionBlock(previewTransaction)
}

@Composable
fun TransactionBlock(transaction: Transaction,
                     modifier: Modifier = Modifier) {
    val categoryDao = AppDatabase.instance.categoryDao()
    val df = DateFormat.getDateInstance(DateFormat.LONG)
    Text(
        text = df.format(transaction.date),
        fontWeight = FontWeight.SemiBold
    )
    Card (
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(12.dp)
        ) {
            Card (
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = categoryDao.getById(transaction.categoryId).color) // TODO may be this could be more effective
            ) {
                Box (
                    modifier = Modifier.padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryDao.getById(transaction.categoryId).icon, // TODO may be this could be more effective
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Column (
                modifier = Modifier
                    .weight(0.65f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = categoryDao.getById(transaction.categoryId).name, // TODO may be this could be more effective
                    fontWeight = FontWeight.SemiBold
                )
                if (transaction.comment != null)
                    Text(
                        text = transaction.comment,
                        fontSize = 14.sp
                    )
            }
            Text(
                text = "${transaction.amount} ₽",
                textAlign = TextAlign.End,
                modifier = Modifier.weight(0.3f)
            )
        }
    }
}