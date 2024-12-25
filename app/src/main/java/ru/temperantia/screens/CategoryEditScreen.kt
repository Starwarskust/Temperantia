package ru.temperantia.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.temperantia.R
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Category
import ru.temperantia.ui.theme.SoftGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen(navHostController: NavHostController) {
    val categoryDao = AppDatabase.instance.categoryDao()
    val categoryList = categoryDao.getAll()
    var n by remember { mutableIntStateOf(categoryList.size) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_categories)) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = SoftGreen,
                    titleContentColor = Color.White
                )
            )
        },
    ) { innerPadding ->
        Surface (
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {



                // TODO remove TEMPORAL BUTTONS
                OutlinedButton(
                    onClick = {
                        categoryDao.insertAll(categoryPresetScope)
                        n += categoryPresetScope.size
                    }
                ) {
                    Text("Push ${categoryPresetScope.size} transactions")
                }
                OutlinedButton(
                    onClick = {
                        categoryDao.deleteAll()
                        n = 0
                    }
                ) {
                    Text("Clean database")
                }
                Text("Now DB has $n categories records")
                // TEMPORAL BUTTONS



                LazyVerticalGrid(
                    modifier = Modifier.padding(12.dp),
                    columns = GridCells.Adaptive(minSize = 96.dp)
                ) {
                    items(categoryList) {
//                        IconButton( onClick = { /*open screen*/ } ) {
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally
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
                                            modifier = Modifier.size(48.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                                Text(it.name)
                            }
//                        }
                    }
                    item { Text("Single item") }
                }
            }
        }
    }
}

val categoryPresetScope = listOf(
    Category(null, "Продукты", Icons.Outlined.ShoppingCart,   Color(0xFFfed766)),
    Category(null, "Дом",      Icons.Outlined.Home, Color(0xFFfe4a49)),
    Category(null, "Здоровье", Icons.Outlined.FavoriteBorder,   Color(0xFF2ab7ca)),
    Category(null, "Отдых",    Icons.Outlined.LocalBar,    SoftGreen),
)