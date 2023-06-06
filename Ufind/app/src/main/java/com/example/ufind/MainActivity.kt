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
import com.example.ufind.screen.ChangePasswordScreen
import com.example.ufind.screen.EnterCodeForgottenPasswordScreen
import com.example.ufind.screen.ForgottenPasswordScreen


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
                onClickUserInterfaceNavigation = { navController.navigate(OptionsRoutes.UserInterface.route) },
                onClickForgottenPasswordScreen = { navController.navigate(OptionsRoutes.ForgottenPassword.route) }
            )
        }
        composable(route = OptionsRoutes.SignUp.route) {
            SignUpScreen({ navController.navigate(OptionsRoutes.LogIn.route) }) {
                navController.navigate(
                    OptionsRoutes.UserInterface.route
                )
            }
        }
        composable(route = OptionsRoutes.UserInterface.route) {
            UserInterfaceNavigation()
        }
        composable(route = OptionsRoutes.ForgottenPassword.route) {
            ForgottenPasswordScreen { navController.navigate(OptionsRoutes.EnterCodeForgottenPasswordScreen.route) }
        }
        composable(route = OptionsRoutes.EnterCodeForgottenPasswordScreen.route) {
            EnterCodeForgottenPasswordScreen { navController.navigate(OptionsRoutes.ChangePasswordScreen.route) }
        }
        composable(route = OptionsRoutes.ChangePasswordScreen.route) {
            ChangePasswordScreen { navController.navigate(OptionsRoutes.LogIn.route) }
        }
    }
}
