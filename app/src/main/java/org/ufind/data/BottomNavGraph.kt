package org.ufind.data
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.ufind.ui.screen.home.MainChatScreen
import org.ufind.ui.screen.home.SavedPostScreen
import org.ufind.ui.screen.home.UserProfileScreen
import org.ufind.ui.screen.settings.SettingsAccountScreen
import org.ufind.ui.screen.settings.SettingsChangePassword
import org.ufind.ui.screen.settings.SettingsPreferencesScreen
import org.ufind.ui.screen.settings.SettingsScreen
import org.ufind.ui.screen.settings.SettingsSecurityScreen
import org.ufind.ui.screen.home.post.add.AddPostScreen
import org.ufind.ui.screen.userpost.addpost.ui.PostScreen
import org.ufind.ui.screen.wallet.WalletProfileScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route ){
        composable(route= BottomBarScreen.Home.route){
            PostScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
        }
        composable(route = BottomBarScreen.Profile.route) {
            UserProfileScreen(
                onClickProfileSettings = { navController.navigate(OptionsRoutes.SettingsScreen.route) },
                onClickWalletButton = { navController.navigate(OptionsRoutes.WalletScreen.route) }
            )
        }
        composable(route = BottomBarScreen.SavedPosts.route) {
            SavedPostScreen()
        }
        composable(route = BottomBarScreen.Chat.route) {
            MainChatScreen()
        }

        AddPostScreen.composable(this, navController)

        composable(route = OptionsRoutes.PostScreen.route) {
            PostScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
        }
        composable(route = OptionsRoutes.SettingsScreen.route) {
            SettingsScreen(
                onClickSettingsAccountScreen = { navController.navigate(OptionsRoutes.SettingsAccountScreen.route) },
                onClickSettingsPreferences = { navController.navigate(OptionsRoutes.SettingsPreferencesScreen.route) },
            ) {
                navController.navigate(
                    OptionsRoutes.SettingsSecurityScreen.route
                )
            }
        }
        composable(route = OptionsRoutes.SettingsPreferencesScreen.route) {
            SettingsPreferencesScreen { navController.navigate(OptionsRoutes.SettingsScreen.route) }
        }
        composable(route = OptionsRoutes.SettingsSecurityScreen.route) {
            SettingsSecurityScreen(
                onClickSettingsScreen = { navController.navigate(OptionsRoutes.SettingsScreen.route) }
            ) {
                navController.navigate(
                    OptionsRoutes.SettingsChangePassword.route
                )
            }
        }
        composable(route = OptionsRoutes.SettingsAccountScreen.route) {
            SettingsAccountScreen { navController.navigate(OptionsRoutes.SettingsScreen.route) }
        }
        composable(route = OptionsRoutes.SettingsChangePassword.route) {
            SettingsChangePassword { navController.navigate(OptionsRoutes.SettingsChangePassword.route) }
        }
        composable(route = OptionsRoutes.WalletScreen.route) {
            WalletProfileScreen()
        }

    }
}