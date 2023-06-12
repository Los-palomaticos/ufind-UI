package org.ufind


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.ufind.data.OptionsRoutes
import org.ufind.ui.navigation.NavigationView
import org.ufind.ui.screen.LogInOrSignUpOptions
import org.ufind.ui.screen.login.LoginScreen
import org.ufind.ui.screen.signup.SignUpScreen
import org.ufind.ui.navigation.UserInterfaceNavigation
import org.ufind.ui.screen.changepassword.ChangePasswordScreen
import org.ufind.ui.screen.changepassword.EnterCodeForgottenPasswordScreen
import org.ufind.ui.screen.changepassword.ForgottenPasswordScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationView()
        }
    }
}

