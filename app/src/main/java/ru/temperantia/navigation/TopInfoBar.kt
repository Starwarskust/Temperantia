package ru.temperantia.navigation

import android.icu.text.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.temperantia.R
import ru.temperantia.ui.theme.SoftGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopInfoBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    contentActions: @Composable (RowScope.() -> Unit) = {}
) {
    val df = DateFormat.getPatternInstance(DateFormat.YEAR_MONTH)
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
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        },
        actions = contentActions,
        colors = topAppBarColors(
            containerColor = SoftGreen,
            titleContentColor = Color.White
        )
    )
}