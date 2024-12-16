package ru.temperantia.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.temperantia.BottomNavigationBar
import ru.temperantia.TopInfoBar
import ru.temperantia.data.OperationRecord

@Composable
fun TransactionScreen(navHostController: NavHostController, operationList: List<OperationRecord>) {
    Scaffold (
        topBar = { TopInfoBar() },
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { innerPadding ->
        Surface (
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                items(
                    items = operationList,
                    key = { item -> item.id }
                ) { operationItem ->
                    TransactionCard(operationItem.category, operationItem.comment, operationItem.cost)
                }
            }
        }
    }
}

@Composable
fun TransactionCard(category: String,
                    comment: String,
                    cost: Float,
                    modifier: Modifier = Modifier) {
    Card (
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(12.dp)
        ) {
            Text("ic     ", color = Color.White)
            Column (
                modifier = Modifier.weight(0.65f)
            ) {
                Text(category)
                Text(comment)
            }
            Text(
                text = "$cost ₽",
                textAlign = TextAlign.End,
                modifier = Modifier.weight(0.3f)
            )
        }
    }
}

val purchaseRecords = listOf(
    OperationRecord(0, "Продукты", "Пятерочка"  , 124.67f),
    OperationRecord(1, "Продукты", "Магнит"     , 345.17f),
    OperationRecord(2, "Продукты", "Перекресток", 123.98f),
    OperationRecord(3, "Развлечения", "ВДНХ"    , 654.67f),
    OperationRecord(4, "Продукты", "Пятерочка"  , 124.67f),
    OperationRecord(5, "Продукты", "Магнит"     , 345.17f),
    OperationRecord(6, "Продукты", "Перекресток", 123.98f),
    OperationRecord(7, "Развлечения", "ВДНХ"    , 654.67f),
    OperationRecord(8, "Продукты", "Пятерочка"  , 124.67f),
    OperationRecord(9, "Продукты", "Магнит"     , 345.17f),
    OperationRecord(10, "Продукты", "Пятерочка"  , 124.67f),
    OperationRecord(11, "Продукты", "Магнит"     , 345.17f),
    OperationRecord(12, "Продукты", "Перекресток", 123.98f),
    OperationRecord(13, "Развлечения", "ВДНХ"    , 654.67f),
    OperationRecord(14, "Продукты", "Пятерочка"  , 124.67f),
    OperationRecord(15, "Продукты", "Магнит"     , 345.17f),
    OperationRecord(16, "Продукты", "Перекресток", 123.98f),
    OperationRecord(17, "Развлечения", "ВДНХ"    , 654.67f),
    OperationRecord(18, "Продукты", "Пятерочка"  , 124.67f),
    OperationRecord(19, "Продукты", "Магнит"     , 345.17f)
)