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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ru.temperantia.BottomNavigationBar
import ru.temperantia.TopInfoBar
import ru.temperantia.data.Category

@Composable
fun CategoryScreen(navHostController: NavHostController) {
    Scaffold (
        topBar = { TopInfoBar() },
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { innerPadding ->
        Surface (
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Column {
                PieInfo(categories, modifier = Modifier.padding(12.dp).fillMaxWidth())
                CategoryField(categories, modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp))
            }
        }
    }
}

@Composable
fun PieInfo(categoryList: List<Category>, modifier: Modifier = Modifier) {
    val sum = categoryList.map { it.totalExpense }.sum()
//    val mContext = LocalContext.current as Activity
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
            Text("Декабрь 2024")
            Box (
                modifier = Modifier.fillMaxWidth()
            ) {
                PieChart(
                    categoryList = categoryList,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
                FloatingActionButton(
                    onClick = { /* mContext.startActivity(Intent(mContext, EntranceNewExpence::class.java)) */ },
                    shape = CircleShape,
                    containerColor = Color.Yellow,
                    modifier = Modifier.align(alignment = Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
                Text(
                    text = "$sum ₽",
                    fontSize = 20.sp,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
        }
    }
}

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
            key = { item -> item.categoryId }
        ) { categoryItem ->
            val relativeExpense = categoryItem.totalExpense / sum * 100
            CategoryCard(categoryItem.categoryName, relativeExpense.toInt(), categoryItem.totalExpense)
        }
    }
}

@Composable
fun CategoryCard(
    categoryName: String,
    relativeExpense: Int,
    totalExpense: Float,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row (
            modifier = modifier.padding(12.dp).fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null
            )
            Text(
                text = categoryName,
                modifier = Modifier.weight(0.65f)
            )
            Text(
                text = "$relativeExpense%",
                modifier = Modifier.weight(0.15f),
                textAlign = TextAlign.End
            )
            Text(
                text = "$totalExpense ₽",
                modifier = Modifier.weight(0.40f),
                textAlign = TextAlign.End
            )
        }
    }
}

val categories = listOf(
    Category(0, "Продукты", 12345.67f, Color.Yellow),
    Category(1, "Дом", 3452.17f, Color.Red),
    Category(2, "Здоровье", 1234.98f, Color.Blue),
    Category(3, "Развлечения", 6546.67f, Color.Green)
)

//@Preview(showBackground = true)
//@Composable
//fun GeneralCardPreview() {
//    TemperantiaTheme {
//        CategoryScreen()
//    }
//}

@Preview(showBackground = true)
@Composable
fun PieInfoPreview() {
    PieInfo(categories)
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    PieChart(categories)
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    CategoryCard("sampleText", 12, 135.76f)
}