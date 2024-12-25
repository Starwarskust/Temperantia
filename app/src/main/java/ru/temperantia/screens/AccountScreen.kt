package ru.temperantia.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import ru.temperantia.navigation.BottomNavigationBar
import ru.temperantia.navigation.MenuDrawer
import ru.temperantia.navigation.TopInfoBar

@Composable
fun AccountScreen(navHostController: NavHostController) {
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
                        imageVector = Icons.Filled.Add,
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
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                Text("AccountScreen")
            }
        }
    }
}