package ru.temperantia.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.temperantia.ui.theme.DeepDarkGreen

@Composable
fun TransactionEntranceScreen() {
    Surface (
        color = DeepDarkGreen,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Enter your new waste", color = Color.White)
    }
}