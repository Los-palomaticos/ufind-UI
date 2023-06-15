package org.ufind.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.ufind.data.OptionsRoutes
import org.ufind.ui.navigation.UserInterfaceNavigation
import org.ufind.ui.screen.start.LoginOrSignUpOptions
import org.ufind.ui.screen.changepassword.ChangePasswordScreen
import org.ufind.ui.screen.changepassword.EnterCodeForgottenPasswordScreen
import org.ufind.ui.screen.changepassword.ForgottenPasswordScreen
import org.ufind.ui.screen.login.LoginScreen
import org.ufind.ui.screen.signup.SignUpScreen

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
//        composable(route = OptionsRoutes.LogIn.route) {
//            LoginScreen(
//                onClickSignUpScreen = { navHostController.navigate(OptionsRoutes.SignUp.route) },
//                onClickUserInterfaceNavigation = { navHostController.navigate(OptionsRoutes.UserInterface.route) },
//                onClickForgottenPasswordScreen = { navHostController.navigate(OptionsRoutes.ForgottenPassword.route) }
//            )
//        }
        SignUpScreen.composable(this, navHostController)
        LoginScreen.composable(this, navHostController)
        composable(route = OptionsRoutes.UserInterface.route) {
            UserInterfaceNavigation()
        }

//        composable(route = OptionsRoutes.SignUp.route) {
//            val viewModel = viewModel<SignUpViewModel>(factory = SignUpViewModel.Factory)
//            val viewModelAsState = viewModel.navigationState.collectAsState()
//            LaunchedEffect(viewModelAsState) {
//                when(viewModelAsState as NavigationState) {
//                    is NavigationState.NavigateToRoute -> {
//                    }
//
//                    else -> {}
//                }
//            }
//            SignUpScreen(viewModel = viewModel /*{ navController.navigate(OptionsRoutes.LogIn.route) }*/)
////            {
////                navController.navigate(
////                    OptionsRoutes.UserInterface.route
////                )
////            }
//        }
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