package social.ufind.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import social.ufind.data.OptionsRoutes
import social.ufind.ui.navigation.UserInterfaceNavigation
import social.ufind.ui.screen.changepassword.ChangePasswordScreen
import social.ufind.ui.screen.changepassword.EnterCodeForgottenPasswordScreen
import social.ufind.ui.screen.changepassword.ForgottenPasswordScreen
import social.ufind.ui.screen.login.LoginScreen
import social.ufind.ui.screen.signup.SignUpScreen
import social.ufind.ui.screen.start.LoginOrSignUpOptions

@Composable
fun NavigationComponent(navHostController: NavHostController, startDestination: String) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = Modifier
    ) {
        composable(route = OptionsRoutes.LogInOrSignUpOptions.route) {
            LoginOrSignUpOptions(
                onClickSignUpScreen = { navHostController.navigate(OptionsRoutes.SignUp.route) },
                onClickSignInScreen = { navHostController.navigate(OptionsRoutes.LogIn.route) }
            )
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