package com.example.ufind

import android.annotation.SuppressLint
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ufind.data.BottomBarScreen
import com.example.ufind.data.BottomNavGraph


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserInterfaceNavigation() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val navigationItem = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Chat,
        BottomBarScreen.SavedPosts,
        BottomBarScreen.Profile
    )
    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { BottomNavigationBar(navController, navigationItem) }
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
fun BottomNavigationBar(navController: NavHostController, navigationItem: List<BottomBarScreen>) {
    androidx.compose.material.BottomAppBar {
        BottomNavigation {
            val currentRoute = currentRoute(navController = navController)
            navigationItem.forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item.route) },
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