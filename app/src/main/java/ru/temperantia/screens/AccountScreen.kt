package ru.temperantia.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.temperantia.navigation.BottomNavigationBar
import ru.temperantia.navigation.TopInfoBar

@Composable
fun AccountScreen(navHostController: NavHostController) {
    Scaffold (
        topBar = { TopInfoBar() },
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { innerPadding ->
        Surface (
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Text("AccountScreen")
        }
    }
}