package social.ufind.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.ufind.R
import social.ufind.data.BottomNavGraph


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun UserInterfaceNavigation() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val navigationItem = listOf(
        social.ufind.data.BottomBarScreen.Home,
        social.ufind.data.BottomBarScreen.Chat,
        social.ufind.data.BottomBarScreen.SavedPosts,
        social.ufind.data.BottomBarScreen.Profile
    )
    val showBottomBar =
        navController.currentBackStackEntryAsState().value?.destination?.route in navigationItem.map { it.route }
    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { if(showBottomBar) {
            BottomNavigationBar(navController, navigationItem)}
        }
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val entryRoute by navController.currentBackStackEntryAsState()
    return entryRoute?.destination?.route
}

@Composable
fun BottomNavigationBar(navController: NavHostController, navigationItem: List<social.ufind.data.BottomBarScreen>) {
    androidx.compose.material.BottomAppBar(backgroundColor = Color.White) {
        BottomNavigation(
            backgroundColor = Color.White, contentColor = colorResource(
                id = R.color.text_color
            )
        ) {
            val currentRoute = currentRoute(navController = navController)

            navigationItem.forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) },
                    alwaysShowLabel = false
                )
            }
        }
    }
}