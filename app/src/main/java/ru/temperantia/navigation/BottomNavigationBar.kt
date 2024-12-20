package ru.temperantia.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.temperantia.AccountNode
import ru.temperantia.CategoryNode
import ru.temperantia.OverviewNode
import ru.temperantia.R
import ru.temperantia.TopLevelRoute
import ru.temperantia.TransactionNode

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {
    val topLevelRoutes = listOf(
        TopLevelRoute(stringResource(R.string.accounts), AccountNode, Icons.Outlined.Wallet),
        TopLevelRoute(stringResource(R.string.categories), CategoryNode, Icons.Outlined.DonutLarge),
        TopLevelRoute(stringResource(R.string.transactions), TransactionNode, Icons.AutoMirrored.Outlined.ListAlt),
        TopLevelRoute(stringResource(R.string.overview), OverviewNode, Icons.Outlined.BarChart)
    )
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        topLevelRoutes.forEachIndexed { _, topLevelRoute ->
            NavigationBarItem(
                icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
                onClick = {
                    navHostController.navigate(topLevelRoute.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}