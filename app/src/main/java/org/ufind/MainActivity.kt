package org.ufind


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ufind.data.OptionsRoutes
import org.ufind.data.datastore.DataStoreManager
import org.ufind.data.model.UserModel
import org.ufind.navigation.NavigationComponent
import org.ufind.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel<MainViewModel>()
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val startDestination = viewModel.startDestination.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            LaunchedEffect(dataStoreManager.getUserData()) {
                lifecycleScope.launch {
                    val user = dataStoreManager.getUserData()
                    user.collect{
                        Log.d("APP_TAG", it.toString())
                        if(it.token != "")
                            viewModel.updateStartDestination(OptionsRoutes.UserInterface.route)
                        else
                            viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
                    }
                }
            }

//            val startDestionation = if (user.token == "") OptionsRoutes.LogInOrSignUpOptions.route else OptionsRoutes.UserInterface.route
            NavigationComponent(navHostController = navController, startDestination = startDestination.value)
//            Log.d("APP_TAG", user.toString())
//            NavigationView()
        }
    }
}
@Composable
fun NavigationView() {
    val navController = rememberNavController()

//    NavHost(
//        navController = navController,
//        startDestination = OptionsRoutes.LogInOrSignUpOptions.route
//    ) {
//        composable(route = OptionsRoutes.LogInOrSignUpOptions.route) {
//            LogInOrSignUpOptions(
//                onClickSignUpScreen = { navController.navigate(OptionsRoutes.SignUp.route) },
//                onClickSignInScreen = { navController.navigate(OptionsRoutes.LogIn.route) }
//            )
//        }
//        composable(route = OptionsRoutes.LogIn.route) {
//            LoginScreen(
//                onClickSignUpScreen = { navController.navigate(OptionsRoutes.SignUp.route) },
//                onClickUserInterfaceNavigation = { navController.navigate(OptionsRoutes.UserInterface.route) },
//                onClickForgottenPasswordScreen = { navController.navigate(OptionsRoutes.ForgottenPassword.route) }
//            )
//        }
//
//        composable(route = OptionsRoutes.SignUp.route) {
//            SignUpScreen( /*{ navController.navigate(OptionsRoutes.LogIn.route) }*/)
////            {
////                navController.navigate(
////                    OptionsRoutes.UserInterface.route
////                )Logi
////            }
//        }
//        composable(route = OptionsRoutes.UserInterface.route) {
//            UserInterfaceNavigation()
//        }
//        composable(route = OptionsRoutes.ForgottenPassword.route) {
//            ForgottenPasswordScreen { navController.navigate(OptionsRoutes.EnterCodeForgottenPasswordScreen.route) }
//        }
//        composable(route = OptionsRoutes.EnterCodeForgottenPasswordScreen.route) {
//            EnterCodeForgottenPasswordScreen { navController.navigate(OptionsRoutes.ChangePasswordScreen.route) }
//        }
//        composable(route = OptionsRoutes.ChangePasswordScreen.route) {
//            ChangePasswordScreen { navController.navigate(OptionsRoutes.LogIn.route) }
//        }
//    }
}
