package ru.temperantia.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import ru.temperantia.R
import ru.temperantia.ui.theme.SoftGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabTopBar(
    onMenuClick: () -> Unit,
    onButtonClick: @Composable RowScope.() -> Unit = {}
) {
//    val df = DateFormat.getPatternInstance(DateFormat.YEAR_MONTH)
    CenterAlignedTopAppBar(
        title = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.all_accounts),
                    fontSize = 14.sp
                )
                Text(
                    // TODO get total Money from accounts table in database
                    text = "12 345.67 ₽",
                    fontSize = 20.sp
                )
//                Text(df.format(Date()))
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        },
        actions = onButtonClick,
        colors = topAppBarColors(
            containerColor = SoftGreen,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun AccountTopBar(onMenuClick: () -> Unit, onButtonClick: () -> Unit) {
    MainTabTopBar(
        onMenuClick = onMenuClick,
        onButtonClick = {
            IconButton(onClick = onButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun CategoryTopBar(onMenuClick: () -> Unit, onButtonClick: () -> Unit) {
    MainTabTopBar(
        onMenuClick = onMenuClick,
        onButtonClick = {
            IconButton(onClick = onButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun TransactionTopBar(onMenuClick: () -> Unit, onButtonClick: () -> Unit) {
    MainTabTopBar(
        onMenuClick = onMenuClick,
        onButtonClick = {
            IconButton(onClick = onButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun OverviewTopBar(onMenuClick: () -> Unit) {
    MainTabTopBar(onMenuClick = onMenuClick)
}