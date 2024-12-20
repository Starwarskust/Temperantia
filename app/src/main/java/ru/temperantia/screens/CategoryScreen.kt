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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.temperantia.InputNode
import ru.temperantia.data.Category
import ru.temperantia.navigation.BottomNavigationBar
import ru.temperantia.navigation.MenuDrawer
import ru.temperantia.navigation.TopInfoBar
import ru.temperantia.ui.theme.SoftGreen
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
                    IconButton(onClick = { /* do something */ }) {
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
                    PieInfo(categoriesRandomScope, navHostController, modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth())
                    CategoryField(categoriesRandomScope, modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp))
                }
            }
        }
    }
}

@Composable
fun PieInfo(categoryList: List<Category>, navHostController: NavHostController, modifier: Modifier = Modifier) {
    val sum = categoryList.map { it.totalExpense }.sum()
    var currentSum by remember { mutableFloatStateOf(sum) }
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
                    categoryList = categoryList,
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
                    text = "$currentSum ₽",
                    fontSize = 20.sp,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PieChartPreview() {
//    PieChart(categoriesRandomScope)
//}

@Composable
fun PieChart(categoryList: List<Category>, modifier: Modifier = Modifier) {
    val sum = categoryList.map { it.totalExpense }.sum()
    Box(modifier = modifier
        .size(250.dp)
        .padding(35.dp)
        .drawBehind {
            var startAngle = -90f
            var sweepAngle: Float
            categoryList.forEach { categoryItem ->
                sweepAngle = categoryItem.totalExpense / sum * 360
                drawArc(
                    color = categoryItem.color,
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
fun CategoryField(categoryList: List<Category>, modifier: Modifier = Modifier) {
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        val sum = categoryList.map { it.totalExpense }.sum()
        items(
            items = categoryList,
            key = { item -> item.id }
        ) { categoryItem ->
            val relativeExpense = categoryItem.totalExpense / sum * 100
            CategoryCard(categoryItem, relativeExpense.toInt())
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CategoryCardPreview() {
//    val previewCategory = Category(
//        id = 0,
//        name = "Продукты",
//        totalExpense = 123.45f,
//        color = Color.Blue
//    )
//    CategoryCard(previewCategory, 15)
//}

@Composable
fun CategoryCard(category: Category, relativeExpense: Int, modifier: Modifier = Modifier) {
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
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = category.color
            )
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
                text = "${category.totalExpense} ₽",
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

val categoriesRandomScope = listOf(
    Category(0, "Продукты", 12345.67f, Color(0xFFfed766)),
    Category(1, "Дом", 3452.17f, Color(0xFFfe4a49)),
    Category(2, "Здоровье", 1234.98f, Color(0xFF2ab7ca)),
    Category(3, "Развлечения", 6546.67f, SoftGreen)
)