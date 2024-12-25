package ru.temperantia.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.temperantia.CategoryEditNode
import ru.temperantia.InputNode
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Category
import ru.temperantia.data.Transaction
import ru.temperantia.navigation.BottomNavigationBar
import ru.temperantia.navigation.MenuDrawer
import ru.temperantia.navigation.TopInfoBar
import ru.temperantia.ui.theme.yellowButton

@Composable
fun CategoryScreen(navHostController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { MenuDrawer() },
    ) {
        Scaffold (
            topBar = {
                TopInfoBar(scope, drawerState) {
                    IconButton(onClick = { navHostController.navigate(CategoryEditNode) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            },
            bottomBar = { BottomNavigationBar(navHostController) }
        ) { innerPadding ->
            Surface (
                color = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column {
                    PieInfo(navHostController, modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth())
                    CategoryField(modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp))
                }
            }
        }
    }
}

@Composable
fun PieInfo(navHostController: NavHostController, modifier: Modifier = Modifier) {
    val categoryMap = AppDatabase.instance.categoryDao().loadCategoryAndTransactionList()
    // TODO take startMoney from account in future
    val startMoney = 20000
    val sum = categoryMap.flatMap { it.value }.sumOf { it.amount }
    val currentSum by remember { mutableDoubleStateOf(sum) }
    val currentMoney = startMoney - currentSum
    Card (
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Box (
                modifier = Modifier.fillMaxWidth()
            ) {
                PieChart(
                    categoryMap = categoryMap,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
                FloatingActionButton(
                    onClick = { navHostController.navigate(InputNode) },
                    containerColor = yellowButton,
                    modifier = Modifier.align(alignment = Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
                Text(
                    text = "%.2f ₽".format(currentSum),
                    fontSize = 20.sp,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun PieChart(categoryMap: Map<Category, List<Transaction>>, modifier: Modifier = Modifier) {
    val sum = categoryMap.flatMap { it.value }.sumOf { it.amount }
    Box(modifier = modifier
        .size(250.dp)
        .padding(35.dp)
        .drawBehind {
            var startAngle = -90f
            var sweepAngle: Float
            categoryMap.forEach { (category, transactionList) ->
                sweepAngle = (transactionList.sumOf { it.amount } / sum * 360).toFloat()
                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 100f, cap = StrokeCap.Butt)
                )
                startAngle += sweepAngle // increase sweep angle
            }
        }
    )
}

@Composable
fun CategoryField(modifier: Modifier = Modifier) {
    val categoryMap = AppDatabase.instance.categoryDao().loadCategoryAndTransactionList()
    val sum = categoryMap.flatMap { it.value }.sumOf { it.amount }
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            items = categoryMap.toList(),
            key = { item -> item.first.id!! }
        ) { item ->
            val totalExpense = item.second.sumOf { it.amount }
            val relativeExpense = totalExpense / sum * 100
            CategoryCard(item.first, relativeExpense.toInt(), totalExpense)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    val previewCategory = Category(
        id = 0,
        name = "Продукты",
        icon = Icons.Outlined.ShoppingCart,
        color = Color.Blue
    )
    CategoryCard(previewCategory, 15, 150.25)
}

@Composable
fun CategoryCard(category: Category, relativeExpense: Int, totalExpense: Double, modifier: Modifier = Modifier) {
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
            modifier = modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Card (
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = category.color)
            ) {
                Box (
                    modifier = Modifier.padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Text(
                text = category.name,
                modifier = Modifier
                    .weight(0.65f)
                    .padding(start = 12.dp)
            )
            Text(
                text = "$relativeExpense%",
                modifier = Modifier.weight(0.15f),
                textAlign = TextAlign.End
            )
            Text(
                text = "%.2f ₽".format(totalExpense),
                modifier = Modifier.weight(0.40f),
                textAlign = TextAlign.End
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GeneralCardPreview() {
//    TemperantiaTheme {
//        CategoryScreen()
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun PieInfoPreview() {
//    PieInfo(categories)
//}