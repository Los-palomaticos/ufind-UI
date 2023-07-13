package social.ufind.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import social.ufind.MainActivity
import social.ufind.ui.screen.changepassword.ChangePasswordScreen
import social.ufind.ui.screen.changepassword.EnterCodeForgottenPasswordScreen
import social.ufind.ui.screen.changepassword.ForgottenPasswordScreen
import social.ufind.ui.screen.start.ConnectionErrorScreen
import social.ufind.ui.screen.start.login.LoginScreen
import social.ufind.ui.screen.start.signup.SignUpScreen
import social.ufind.ui.screen.start.loading.LoadingScreen
import social.ufind.ui.screen.start.LoginOrSignUpOptions

@Composable
fun NavigationComponent(navHostController: NavHostController, startDestination: String) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = Modifier
    ) {
        composable(route = OptionsRoutes.LoadingScreen.route) {
            LoadingScreen()
        }

        composable(route = OptionsRoutes.LogInOrSignUpOptions.route) {
            LoginOrSignUpOptions(
                onClickSignUpScreen = { navHostController.navigate(OptionsRoutes.SignUp.route) },
                onClickSignInScreen = { navHostController.navigate(OptionsRoutes.LogIn.route) }
            )
        }
        composable(route = OptionsRoutes.ConnectionError.route) {
            ConnectionErrorScreen{

            }
        }
        SignUpScreen.composable(this, navHostController)
        LoginScreen.composable(this, navHostController)
        composable(route = OptionsRoutes.UserInterface.route) {
            UserInterfaceNavigation()
        }
        composable(route = OptionsRoutes.ForgottenPassword.route) {
            ForgottenPasswordScreen { navHostController.navigate(OptionsRoutes.EnterCodeForgottenPasswordScreen.route) }
        }
        composable(route = OptionsRoutes.EnterCodeForgottenPasswordScreen.route) {
            EnterCodeForgottenPasswordScreen { navHostController.navigate(OptionsRoutes.ChangePasswordScreen.route) }
        }
        composable(route = OptionsRoutes.ChangePasswordScreen.route) {
            ChangePasswordScreen { navHostController.navigate(OptionsRoutes.LogIn.route) }
        }

    }
}