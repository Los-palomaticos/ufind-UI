package org.ufind.data

sealed class OptionsRoutes(val route:String){
    object LogInOrSignUpOptions: OptionsRoutes("LogInOrSignUpOptions")
    object LogIn: OptionsRoutes("LogInScreen")
    object SignUp: OptionsRoutes("SignUpScreen")
    object UserInterface: OptionsRoutes("UserInterfaceNavigation")
    object ForgottenPassword: OptionsRoutes("ForgottenPasswordScreen")
    object EnterCodeForgottenPasswordScreen: OptionsRoutes("EnterCodeForgottenPasswordScreen")
    object ChangePasswordScreen: OptionsRoutes("ChangePasswordScreen")
    object AddPostScreen: OptionsRoutes("AddPostScreen")
    object PostScreen: OptionsRoutes("PostScreen")
    object SettingsAccountScreen: OptionsRoutes("SettingsAccountScreen")
    object SettingsScreen: OptionsRoutes("SettingsScreen")
    object SettingsPreferencesScreen: OptionsRoutes("SettingsPreferencesScreen")
    object SettingsSecurityScreen: OptionsRoutes("SettingsSecurityScreen")
    object SettingsChangePassword: OptionsRoutes("SettingsChangePassword")
    object WalletScreen: OptionsRoutes("WalletProfileScreen")

    object Home: OptionsRoutes("Home")
}