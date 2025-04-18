package ru.temperantia

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.temperantia.navigation.AccountTopBar
import ru.temperantia.navigation.CategoryTopBar
import ru.temperantia.navigation.MenuDrawer
import ru.temperantia.navigation.OverviewTopBar
import ru.temperantia.navigation.SimpleTopBar
import ru.temperantia.navigation.TransactionTopBar
import ru.temperantia.screens.AboutScreen
import ru.temperantia.screens.AccountScreen
import ru.temperantia.screens.CategoryEditScreen
import ru.temperantia.screens.CategoryScreen
import ru.temperantia.screens.InputScreen
import ru.temperantia.screens.OverviewScreen
import ru.temperantia.screens.SettingScreen
import ru.temperantia.screens.TransactionScreen
import ru.temperantia.ui.theme.TemperantiaTheme

@Composable
fun TemperantiaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val mainTabs = listOf(MainTab.Account, MainTab.Category, MainTab.Transaction, MainTab.Overview)
    val currentRoute = navBackStackEntry?.destination?.route

    val isRootScreen = mainTabs.any { it.rootRoute == currentRoute }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentRoute) {
        if (!isRootScreen && drawerState.isOpen) {
            drawerState.close()
        }
    }

    TemperantiaTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = isRootScreen,
            drawerContent = {
                MenuDrawer(
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    onClose = { scope.launch { drawerState.close() } }
                )
            },
        ) {
            Scaffold (
                topBar = {
                    DynamicTopBar(
                        navController,
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = isRootScreen,
                        enter = expandVertically() + fadeIn()
                    ) {
                        NavigationBar (
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            mainTabs.forEach { tab ->
                                NavigationBarItem(
                                    selected = currentRoute == tab.route,
                                    onClick = {
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(tab.icon, contentDescription = tab.title) },
                                    label = { Text(tab.title) }
                                )
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceDim
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = MainTab.Category.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    accountTab(navController)
                    categoryTab(navController)
                    transactionTab(navController)
                    overviewTab(navController)
                    settingsGraph(navController)
                    aboutGraph(navController)
                }
            }
        }
    }
}

fun NavGraphBuilder.accountTab(navController: NavHostController) {
    navigation(
        startDestination = "account/main",
        route = MainTab.Account.route
    ) {
        composable("account/main") {
            AccountScreen()
        }
    }
}

fun NavGraphBuilder.categoryTab(navController: NavHostController) {
    navigation(
        startDestination = "category/main",
        route = MainTab.Category.route
    ) {
        composable("category/main") {
            CategoryScreen(
                onNavigateToInputScreen = { navController.navigate("transaction/input") }
            )
        }
        composable("category/edit") {
            CategoryEditScreen()
        }
    }
}

fun NavGraphBuilder.transactionTab(navController: NavHostController) {
    navigation(
        startDestination = "transaction/main",
        route = MainTab.Transaction.route
    ) {
        composable("transaction/main") {
            TransactionScreen(
                onNavigateToInputScreen = { navController.navigate("transaction/input") }
            )
        }
        composable("transaction/input") {
            InputScreen(
                onAddTransaction = { navController.popBackStack() }
            )
        }
    }
}

fun NavGraphBuilder.overviewTab(navController: NavHostController) {
    navigation(
        startDestination = "overview/main",
        route = MainTab.Overview.route
    ) {
        composable("overview/main") {
            OverviewScreen()
        }
    }
}

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        startDestination = "settings/main",
        route = "settings"
    ) {
        composable("settings/main") {
            SettingScreen()
        }
    }
}

fun NavGraphBuilder.aboutGraph(navController: NavHostController) {
    navigation(
        startDestination = "about/main",
        route = "about"
    ) {
        composable("about/main") {
            AboutScreen()
        }
    }
}

sealed class MainTab(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val rootRoute: String
) {
    object Account : MainTab(
        route = "account",
        title = MyApplication.appContext.getString(R.string.accounts),
        icon = Icons.Outlined.Wallet,
        rootRoute = "account/main"
    )
    object Category : MainTab(
        route = "category",
        title = MyApplication.appContext.getString(R.string.categories),
        icon = Icons.Outlined.DonutLarge,
        rootRoute = "category/main"
    )
    object Transaction : MainTab(
        route = "transaction",
        title = MyApplication.appContext.getString(R.string.transactions),
        icon = Icons.AutoMirrored.Outlined.ListAlt,
        rootRoute = "transaction/main"
    )
    object Overview : MainTab(
        route = "overview",
        title = MyApplication.appContext.getString(R.string.overview),
        icon = Icons.Outlined.BarChart,
        rootRoute = "overview/main"
    )
}

@Composable
fun DynamicTopBar(navController: NavHostController, onMenuClick: () -> Unit) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    when (currentRoute) {
        "account/main" -> AccountTopBar(
            onMenuClick = onMenuClick,
            onButtonClick = {}
        )
        "category/main" -> CategoryTopBar(
            onMenuClick = onMenuClick,
            onButtonClick = { navController.navigate("category/edit") }
        )
        "transaction/main" -> TransactionTopBar(
            onMenuClick = onMenuClick,
            onButtonClick = {}
        )
        "overview/main" -> OverviewTopBar( onMenuClick = onMenuClick )

        "category/edit" -> SimpleTopBar(
            title = stringResource(R.string.edit_categories),
            onBack = { navController.popBackStack() }
        )
        "transaction/input" -> SimpleTopBar(
            title = stringResource(R.string.add_transaction),
            onBack = { navController.popBackStack() }
        )

        "settings/main" -> SimpleTopBar(
            title = stringResource(R.string.settings),
            onBack = { navController.popBackStack() }
        )
        "about/main" -> SimpleTopBar(
            title = stringResource(R.string.about),
            onBack = { navController.popBackStack() }
        )
    }
}