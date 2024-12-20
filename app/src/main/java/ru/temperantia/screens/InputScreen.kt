package ru.temperantia.screens

import android.icu.text.DateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.temperantia.MyApplication
import ru.temperantia.R
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Transaction
import ru.temperantia.ui.theme.SoftGreen
import ru.temperantia.ui.theme.yellowButton
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(navHostController: NavHostController) {
    val transactionDao = AppDatabase.instance.transactionDao()
    var inputValue by remember { mutableStateOf("0") }
    var inputDate by remember { mutableStateOf(Date()) }
    val transactionList = transactionDao.getAll()
    var n by remember { mutableIntStateOf(transactionList.size) }

//    TODO get lastTransactionDate from database
    val lastTransactionDate = Date(2024 - 1900, 11, 15)

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_transaction)) },
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
            ExtendedFloatingActionButton(
                onClick = {
                    val inputAmount = inputValue.toDouble()
                    if (inputAmount > 0) {
                        val inputTransaction = Transaction(
                            id = null,
                            date = Date(),
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
                Text(stringResource(R.string.add))
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
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Введите сумму") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                DateSelectionPanel(lastTransactionDate) {
                    inputDate = it
                }

                // TEMPORAL BUTTONS
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
                Text("Date:")
                val okda = DateFormat.getPatternInstance(DateFormat.NUM_MONTH_DAY).format(inputDate)
                Text(
                    text = okda,
                    fontWeight = FontWeight.SemiBold
                )
                // TEMPORAL BUTTONS

            }
        }
    }
}

// TODO fix UI response
@Composable
fun DateSelectionPanel(lastTransactionDate: Date, onDateSelected: (Date) -> Unit) {
    val df = DateFormat.getPatternInstance(DateFormat.NUM_MONTH_DAY)

    val calendar = Calendar.getInstance()
    val today = calendar.time
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    val yesterday = calendar.time

    var thirdChipDate by remember { mutableStateOf(lastTransactionDate) }
    var thirdChipText by remember { mutableStateOf(MyApplication.appContext.getString(R.string.last)) }

    var selectedDate by remember { mutableStateOf(Date()) }

    var todayIsSelected by remember { mutableStateOf(true) }
    var yesterdayIsSelected by remember { mutableStateOf(false) }
    var thirdChipIsSelected by remember { mutableStateOf(false) }

    var calendarWasOpened by remember { mutableStateOf(false) }
    var showModal by remember { mutableStateOf(false) }

    Row (
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        FilterChip(
            onClick = {
                todayIsSelected = true
                yesterdayIsSelected = false
                thirdChipIsSelected = false
                onDateSelected(today)
            },
            label = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(df.format(today))
                    Text(stringResource(R.string.today))
                }
            },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SoftGreen),
            selected = todayIsSelected
        )
        when {
            !calendarWasOpened -> {

            }
        }
        FilterChip(
            onClick = {
                todayIsSelected = false
                yesterdayIsSelected = true
                thirdChipIsSelected = false
                onDateSelected(yesterday)
            },
            label = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(df.format(yesterday))
                    Text(stringResource(R.string.yesterday))
                }
            },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SoftGreen),
            selected = yesterdayIsSelected
        )
        FilterChip(
            onClick = {
                todayIsSelected = false
                yesterdayIsSelected = false
                thirdChipIsSelected = true
                onDateSelected(thirdChipDate)
            },
            label = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(df.format(thirdChipDate))
                    Text(thirdChipText)
                }
            },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SoftGreen),
            selected = thirdChipIsSelected
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { showModal = true }) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = SoftGreen
            )
        }
        if (showModal) {
            DatePickerModal(
                onDateSelected = {
                    if (it != null) {
                        selectedDate = Date(it)
                        onDateSelected(selectedDate)
                        showModal = false
                        calendarWasOpened = true
                        when {
                            selectedDate.before(yesterday) -> {
                                todayIsSelected = false
                                yesterdayIsSelected = false
                                thirdChipIsSelected = true
                                thirdChipDate = selectedDate
                                thirdChipText = MyApplication.appContext.getString(R.string.selected)
                            }
                            selectedDate == yesterday -> {
                                todayIsSelected = false
                                yesterdayIsSelected = true
                                thirdChipIsSelected = false
                            }
                            selectedDate == today -> {
                                todayIsSelected = true
                                yesterdayIsSelected = false
                                thirdChipIsSelected = false
                            }
                        }
                    } else {
                        showModal = false
                    }
                },
                onDismiss = { showModal = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

val randomScope = listOf(
    Transaction(null, Date(2024 - 1900, 11, 15), "Карта", "Продукты", "Подкатегория", 124.67, "Пятерочка"),
    Transaction(null, Date(2024 - 1900, 11, 16), "Карта", "Дом",      "Подкатегория", 345.17, null),
    Transaction(null, Date(2024 - 1900, 11, 16), "Карта", "Продукты", "Подкатегория", 1023.0,   "Перекресток"),
    Transaction(null, Date(2024 - 1900, 11, 18), "Карта", "Здоровье", null,           350.10, "Горздрав"),
    Transaction(null, Date(2024 - 1900, 11, 19), "Карта", "Продукты", "Подкатегория", 345.17, null)
)