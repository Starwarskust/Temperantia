package ru.temperantia.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.temperantia.InputNode
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Transaction
import ru.temperantia.navigation.BottomNavigationBar
import ru.temperantia.navigation.MenuDrawer
import ru.temperantia.navigation.TopInfoBar
import ru.temperantia.ui.theme.yellowButton

@Composable
fun TransactionScreen(navHostController: NavHostController) {
    val transactionDao = AppDatabase.instance.transactionDao()
    val transactionList = transactionDao.getAll()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { MenuDrawer() },
    ) {
        Scaffold (
        topBar = { TopInfoBar(scope, drawerState) },
            bottomBar = { BottomNavigationBar(navHostController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navHostController.navigate(InputNode) },
                    containerColor = yellowButton
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
            }
        ) { innerPadding ->
            Surface (
                color = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn (
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = transactionList.reversed(),
                        key = { item -> item.id!! }
                    ) { transactionItem ->
                        TransactionCard(transactionItem)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionCardPreview() {
    val previewTransaction = Transaction(
        id = null,
        date = 0,
        account = "Карта",
        category = "Продукты",
        subcategory = null,
        amount = 123.45,
        comment = "Комментарий"
    )
    TransactionCard(previewTransaction)
}

@Composable
fun TransactionCard(transaction: Transaction,
                    modifier: Modifier = Modifier) {
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
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = null,
                tint = Color.Blue
            )
            Column (
                modifier = Modifier
                    .weight(0.65f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = transaction.category,
                    fontWeight = FontWeight.SemiBold
                )
                if (transaction.comment != null)
                    Text(
                        text = transaction.comment,
                        fontSize = 12.sp
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