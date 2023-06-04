package com.example.ufind


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ufind.data.OptionsRoutes
import com.example.ufind.screen.LogInOrSignUpOptions
import com.example.ufind.screen.LoginScreen
import com.example.ufind.screen.SignUpScreen
import com.example.ufind.navigation.UserInterfaceNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationView()
        }
    }
}

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = OptionsRoutes.LogInOrSignUpOptions.route
    ) {
        composable(route = OptionsRoutes.LogInOrSignUpOptions.route) {
            LogInOrSignUpOptions(
                onClickSignUpScreen = { navController.navigate(OptionsRoutes.SignUp.route) },
                onClickSignInScreen = { navController.navigate(OptionsRoutes.LogIn.route) }
            )
        }
        composable(route = OptionsRoutes.LogIn.route) {
            LoginScreen(
                onClickSignUpScreen = { navController.navigate(OptionsRoutes.SignUp.route) },
                onClickUserInterfaceNavigation = {navController.navigate(OptionsRoutes.UserInterface.route)}
            )
        }
        composable(route = OptionsRoutes.SignUp.route) {
            SignUpScreen()
        }
        composable(route = OptionsRoutes.UserInterface.route){
            UserInterfaceNavigation()
        }
    }
}
