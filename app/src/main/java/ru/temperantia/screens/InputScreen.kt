package ru.temperantia.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.temperantia.MyApplication
import ru.temperantia.R
import ru.temperantia.data.AppDatabase
import ru.temperantia.data.Transaction
import ru.temperantia.ui.theme.SoftGreen
import ru.temperantia.ui.theme.yellowButton
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(onAddTransaction: () -> Unit) {
    val transactionDao = AppDatabase.instance.transactionDao()
    var inputValue by remember { mutableStateOf("0") }
    var inputAccount by remember { mutableStateOf<Int?>(0) }
    var inputCategory by remember { mutableStateOf<Int?>(null) }
    var inputDate by remember { mutableStateOf(LocalDateTime.now()) }
    var inputComment by remember { mutableStateOf<String?>(null) }
    val latestExpenseDate = transactionDao.getLatestExpenseDate()

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            OutlinedTextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text("Введите сумму") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            CategorySelectionPanel {
                inputCategory = it
            }
            DateSelectionPanel(latestExpenseDate) {
                inputDate = it
            }
            OutlinedTextField(
                value = if (inputComment != null) inputComment!! else "",
                onValueChange = { inputComment = it },
                label = { Text("Комментарий") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
        ExtendedFloatingActionButton(
            onClick = {
                val inputAmount = inputValue.toDouble()
                if (inputAmount > 0 && inputCategory != null) {
                    val inputTransaction = Transaction(
                        id = null,
                        date = inputDate,
                        accountId = inputAccount!!,
                        categoryId = inputCategory!!,
                        subcategory = null,
                        amount = inputValue.toDouble(),
                        comment = inputComment)
                    transactionDao.insert(inputTransaction)
                }
                onAddTransaction()
            },
            containerColor = yellowButton,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 40.dp),
                text = stringResource(R.string.add)
            )
        }
    }
}

@Composable
fun CategorySelectionPanel(onCategorySelected: (Int) -> Unit) {
    val categoryDao = AppDatabase.instance.categoryDao()
    val categoryList = categoryDao.getAll()
    var selectedIndex by remember { mutableIntStateOf(0) }
    Column {
        Text("Категории", fontSize = 12.sp)
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            categoryList.forEach { category ->
                FilterChip(
                    onClick = {
                        selectedIndex = category.id!!
                        onCategorySelected(category.id)
                    },
                    label = {
                        Text(category.name, fontSize = 16.sp)
                    },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = category.color),
                    selected = (selectedIndex == category.id)
                )
            }
        }
    }
}

// TODO fix UI response
@Composable
fun DateSelectionPanel(latestExpenseDate: LocalDateTime, onDateSelected: (LocalDateTime) -> Unit) {
    val df = DateTimeFormatter.ofPattern("dd.MM")
    val today = LocalDateTime.now()
    val yesterday = today.minusDays(1)

    var thirdChipDate by remember { mutableStateOf(latestExpenseDate) }
    var thirdChipText by remember { mutableStateOf(MyApplication.appContext.getString(R.string.date_last)) }

    var selectedDate by remember { mutableStateOf(LocalDateTime.now()) }

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
                    Text(stringResource(R.string.date_today))
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
                    Text(stringResource(R.string.date_yesterday))
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
                        selectedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                        onDateSelected(selectedDate)
                        showModal = false
                        calendarWasOpened = true
                        when {
                            selectedDate.isBefore(yesterday) -> {
                                todayIsSelected = false
                                yesterdayIsSelected = false
                                thirdChipIsSelected = true
                                thirdChipDate = selectedDate
                                thirdChipText = MyApplication.appContext.getString(R.string.date_selected)
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