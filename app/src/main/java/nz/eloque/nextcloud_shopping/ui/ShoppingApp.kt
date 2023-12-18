package nz.eloque.nextcloud_shopping.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nz.eloque.nextcloud_shopping.R
import nz.eloque.nextcloud_shopping.ui.settings.SettingsView
import nz.eloque.nextcloud_shopping.ui.shopping_list.ShoppingListView

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object ShoppingList : Screen("list", Icons.Filled.ShoppingCart, R.string.shopping_list)
    object Settings : Screen("settings", Icons.Filled.Settings, R.string.settings)
}

@Composable
fun ShoppingApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Surface(
        modifier = modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.ShoppingList.route,
        ) {
            composable(Screen.ShoppingList.route) {
                ShoppingScaffold(
                    navController = navController,
                    title = stringResource(id = R.string.shopping_list)
                ) {
                    ShoppingListView()
                }
            }
            composable(Screen.Settings.route) {
                ShoppingScaffold(
                    navController = navController,
                    title = stringResource(id = R.string.shopping_list)
                ) {
                    SettingsView()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.shopping),
    toolWindow: Boolean = false,
    showBack: Boolean = true,
    content: @Composable () -> Unit,
) {
    val items = listOf(
        Screen.ShoppingList,
        Screen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (toolWindow && showBack) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    }
                },
                actions = {
                    if (!toolWindow) {
                        IconButton(onClick = {
                            //TODO
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Sync,
                                contentDescription = stringResource(R.string.update)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!toolWindow) {
                BottomAppBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.route) },
                            label = { Text(stringResource(screen.resourceId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
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
        }
    ) { innerPadding ->
        Box(modifier = modifier
            .padding(innerPadding)
            .padding(10.dp)) {
            content.invoke()
        }
    }
}
