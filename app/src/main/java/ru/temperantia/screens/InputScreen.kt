package ru.temperantia.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import ru.temperantia.R
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Transaction
import ru.temperantia.ui.theme.SoftGreen
import ru.temperantia.ui.theme.yellowButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(navHostController: NavHostController) {
    val transactionDao = AppDatabase.instance.transactionDao()
    var inputValue by remember { mutableStateOf("0") }
    val currentTransactionList = transactionDao.getAll()
    var n by remember { mutableIntStateOf(currentTransactionList.size) }
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.add_transaction))
                },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val inputAmount = inputValue.toDouble()
                    if (inputAmount > 0) {
                        val inputTransaction = Transaction(
                            id = null,
                            date = 0,
                            account = "Карта",
                            category = "Продукты",
                            subcategory = null,
                            amount = inputValue.toDouble(),
                            comment = null)
                        transactionDao.insert(inputTransaction)
                    }
                    navHostController.navigateUp()
                },
                containerColor = yellowButton
            ) {
                Text("Add")
            }
        }
    ) { innerPadding ->
        Surface (
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Введите сумму") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedButton(
                    onClick = {
                        transactionDao.insertAll(randomScope)
                        n += randomScope.size
                    }
                ) {
                    Text("Push ${randomScope.size} transactions")
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
    }
}

val randomScope = listOf(
    Transaction(null, 0, "Карта", "Продукты",    "Подкатегория", 124.67, "Пятерочка"  ),
    Transaction(null, 0, "Карта", "Продукты",    "Подкатегория", 345.17, "Магнит"     ),
    Transaction(null, 0, "Карта", "Продукты",    "Подкатегория", 123.98, "Перекресток"),
    Transaction(null, 0, "Карта", "Продукты",    "Подкатегория", 124.67, "Пятерочка"  ),
    Transaction(null, 0, "Карта", "Продукты",    "Подкатегория", 345.17, "Магнит"     )
)