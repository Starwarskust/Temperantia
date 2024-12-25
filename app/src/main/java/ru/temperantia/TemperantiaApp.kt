package ru.temperantia

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.temperantia.screens.AccountScreen
import ru.temperantia.screens.CategoryEditScreen
import ru.temperantia.screens.CategoryScreen
import ru.temperantia.screens.OverviewScreen
import ru.temperantia.screens.InputScreen
import ru.temperantia.screens.TransactionScreen
import ru.temperantia.ui.theme.TemperantiaTheme

@Composable
fun TemperantiaApp() {
    val navController = rememberNavController()
    TemperantiaTheme {
        NavHost(navController = navController, startDestination = CategoryNode) {
            composable<AccountNode> {
                AccountScreen(navController)
            }
            composable<CategoryNode> {
                CategoryScreen(navController)
            }
            composable<TransactionNode> {
                TransactionScreen(navController)
            }
            composable<OverviewNode> {
                OverviewScreen(navController)
            }
            composable<InputNode> {
                InputScreen(navController)
            }
            composable<CategoryEditNode> {
                CategoryEditScreen(navController)
            }
        }
    }
}

@Serializable
object AccountNode

@Serializable
object CategoryNode

@Serializable
object TransactionNode

@Serializable
object OverviewNode

@Serializable
object InputNode

@Serializable
object CategoryEditNode

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)