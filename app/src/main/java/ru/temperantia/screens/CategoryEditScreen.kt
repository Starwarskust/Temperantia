package ru.temperantia.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.temperantia.data.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen() {
    val categoryDao = AppDatabase.instance.categoryDao()
    val categoryList = categoryDao.getAll()
    var n by remember { mutableIntStateOf(categoryList.size) }
    Column {
        LazyVerticalGrid(
            modifier = Modifier.padding(12.dp),
            columns = GridCells.Adaptive(minSize = 80.dp)
        ) {
            items(categoryList) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { n += 100 }
                ) {
                    Card (
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = it.color)
                    ) {
                        Box (
                            modifier = Modifier.padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = Color.White
                            )
                        }
                    }
                    Text(
                        text = it.name,
                        fontSize = 14.sp
                    )
                }
            }
            item { Text("Single item") }
        }
    }
}