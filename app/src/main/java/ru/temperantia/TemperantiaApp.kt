package ru.temperantia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.temperantia.screens.AccountScreen
import ru.temperantia.screens.CategoryScreen
import ru.temperantia.screens.OverviewScreen
import ru.temperantia.screens.TransactionScreen
import ru.temperantia.screens.purchaseRecords
import ru.temperantia.ui.theme.TemperantiaTheme

@Composable
fun TemperantiaApp() {
    val navController = rememberNavController()
    TemperantiaTheme {
        NavHost(navController = navController, startDestination = CategoryButton) {
            composable<AccountButton> {
                AccountScreen(navController)
            }
            composable<CategoryButton> {
                CategoryScreen(navController)
            }
            composable<TransactionButton> {
                TransactionScreen(navController, purchaseRecords)
            }
            composable<OverviewButton> {
                OverviewScreen(navController)
            }
        }
    }

}

@Serializable
object AccountButton

@Serializable
object CategoryButton

@Serializable
object TransactionButton

@Serializable
object OverviewButton

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Счета", AccountButton, Icons.Outlined.AccountBox),
    TopLevelRoute("Категории", CategoryButton, Icons.Outlined.Home),
    TopLevelRoute("Операции", TransactionButton, Icons.Outlined.ShoppingCart),
    TopLevelRoute("Обзор", OverviewButton, Icons.Outlined.Info)
)