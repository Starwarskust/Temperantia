package ru.temperantia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.temperantia.ui.theme.DeepDarkGreen
import ru.temperantia.ui.theme.GreyDarkGreen
import ru.temperantia.ui.theme.TemperantiaTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperantiaTheme {
                GeneralCard()
            }
        }
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            24, TimeUnit.HOURS,
            1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this)
            .enqueue(workRequest)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GeneralCardPreview() {
//    GeneralCard()
//}

@Composable
fun GeneralCard(modifier: Modifier = Modifier) {
    Surface (
        color = DeepDarkGreen,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            UpperBar(modifier)
            PieInfo(modifier)
            CategoryField(modifier)
        }
    }
}

@Composable
fun UpperBar(modifier: Modifier = Modifier) {
    Row {
        val mContext = LocalContext.current as Activity
        OutlinedButton(
            onClick = {
                mContext.startActivity(Intent(mContext, MainMenu::class.java))
            }
        ) {
            Text("icon", color = Color.White)
        }
        Text(
            text = "10 123.56 ₽",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
        )
        OutlinedButton(
            modifier = Modifier,
            onClick = {
//                TODO()
            }
        ) {
            Text("Расходы", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PieInfoPreview() {
    PieInfo()
}

@Composable
fun PieInfo(modifier: Modifier = Modifier) {
    Card (
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(horizontal = 10.dp).fillMaxWidth()
    ) {
        Column (
            modifier = modifier
                .background(GreyDarkGreen)
                .padding(12.dp)
                .fillMaxWidth()
//                .height(600.dp) // ИСКУССТВЕННО ПОВЫСИТЬ РАЗМЕР КРУЖОЧКА, ЧТОБЫ ПОКАЗАТЬ СПИСОК
        ) {
            Text(
                text = "Ноябрь 2024",
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row (
                modifier = modifier.align(Alignment.End)
            ) {
                PieChart(modifier = Modifier.padding(30.dp))
                val mContext = LocalContext.current as Activity
                OutlinedButton(
                    modifier = Modifier.align(Alignment.Bottom),
                    onClick = {
                        mContext.startActivity(Intent(mContext, EntranceNewExpence::class.java))
                    }
                ) {
                    Text("+", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun PieChart(modifier: Modifier = Modifier) {
    // Загрузить с базы данных?
    val categories = listOf(
        Category(0, "Продукты", 12345.67f, Color.Yellow),
        Category(1, "Дом", 3452.17f, Color.Red),
        Category(2, "Здоровье", 1234.98f, Color.Blue),
        Category(3, "Развлечения", 6546.67f, Color.Green)
    )
    // val sum = categories.sumOf{ it.totalExpense } ???
    val sum = 12345.67f + 3452.17f + 1234.98f + 6546.67f
    Box(modifier = modifier
        .size(150.dp)
        .drawBehind {
            var startAngle = -90f
            var sweepAngle: Float
            categories.forEach { categoryItem ->
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
fun CategoryField(modifier: Modifier = Modifier) {
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(10.dp)
    ) {
        // Загрузить с базы данных?
        val categories = listOf(
            Category(0, "Продукты", 12345.67f, Color.Yellow),
            Category(1, "Дом", 3452.17f, Color.Red),
            Category(2, "Здоровье", 1234.98f, Color.Blue),
            Category(3, "Развлечения", 6546.67f, Color.Green)
        )
        // val sum = categories.sumOf{ it.totalExpense } ???
        val sum = 12345.67f + 3452.17f + 1234.98f + 6546.67f
        items(
            items = categories,
            key = { item -> item.categoryId }
        ) { categoryItem ->
            val relativeExpense = categoryItem.totalExpense / sum * 100
            CategoryCard(categoryItem.categoryName, relativeExpense.toInt(), categoryItem.totalExpense)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    CategoryCard("sampleText", 12, 135.76f)
}

@Composable
fun CategoryCard(categoryName: String,
                 relativeExpense: Int,
                 totalExpense: Float,
                 modifier: Modifier = Modifier) {
    Card (
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row (
            modifier = modifier
                .background(GreyDarkGreen)
                .padding(12.dp)
        ) {
            Text("icon  ", color = Color.White)
            Text(
                text = categoryName,
                color = Color.White,
                modifier = Modifier.weight(0.65f)
            )
            Text(
                text = "$relativeExpense%",
                color = Color.White,
                modifier = Modifier.weight(0.15f)
            )
            Text(
                text = "$totalExpense ₽",
                color = Color.White,
                modifier = Modifier.weight(0.33f)
            )
        }
    }
}

data class Category(
    val categoryId: Int,
    val categoryName: String,
    val totalExpense: Float,
    val color: Color
)

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TemperantiaTheme {
//        Greeting("Android")
//    }
//}