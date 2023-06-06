package com.example.ufind.data

sealed class OptionsRoutes(val route:String){
    object LogInOrSignUpOptions: OptionsRoutes("LogInOrSignUpOptions")
    object LogIn: OptionsRoutes("LogInScreen")
    object SignUp: OptionsRoutes("SignUpScreen")
    object UserInterface: OptionsRoutes("UserInterfaceNavigation")
    object ForgottenPassword: OptionsRoutes("ForgottenPasswordScreen")
    object EnterCodeForgottenPasswordScreen: OptionsRoutes("EnterCodeForgottenPasswordScreen")
    object ChangePasswordScreen: OptionsRoutes("ChangePasswordScreen")

}