package social.ufind.data
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import social.ufind.ui.screen.home.MainChatScreen
import social.ufind.ui.screen.home.SavedPostScreen
import social.ufind.ui.screen.home.user.UserProfileScreen
import social.ufind.ui.screen.home.post.PostScreen
import social.ufind.ui.screen.home.post.add.AddPostScreen
import social.ufind.ui.screen.map.MapScreen
import social.ufind.ui.screen.settings.SettingsAccountScreen
import social.ufind.ui.screen.settings.SettingsChangePassword
import social.ufind.ui.screen.settings.SettingsPreferencesScreen
import social.ufind.ui.screen.settings.SettingsScreen
import social.ufind.ui.screen.settings.SettingsSecurityScreen
import social.ufind.ui.screen.wallet.WalletProfileScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = social.ufind.data.BottomBarScreen.Home.route ){
//        composable(route= BottomBarScreen.Home.route){
//            PostScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
//        }
        composable(route = social.ufind.data.BottomBarScreen.Profile.route) {
            UserProfileScreen(
                onClickProfileSettings = { navController.navigate(OptionsRoutes.SettingsScreen.route) },
                onClickWalletButton = { navController.navigate(OptionsRoutes.WalletScreen.route) }
            )
        }
        composable(route = social.ufind.data.BottomBarScreen.SavedPosts.route) {
            SavedPostScreen()
        }
        composable(route = social.ufind.data.BottomBarScreen.Chat.route) {
            MainChatScreen()
        }

        AddPostScreen.composable(this, navController)
        PostScreen.composable(this, navController)
//        composable(route = OptionsRoutes.PostScreen.route) {
//            PostScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
//        }

        SettingsScreen.composable(this, navController)

//        composable(route = OptionsRoutes.PostScreen.route) {
//            PostScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
//        }
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
            SettingsChangePassword(
                onClickBackToSettings = { navController.navigate(OptionsRoutes.SettingsScreen.route) },
                onClickSettingsSecurityScreen = { navController.navigate(OptionsRoutes.SettingsSecurityScreen.route) })
        }
        composable(route = OptionsRoutes.WalletScreen.route) {
            WalletProfileScreen()
        }
        composable(route = OptionsRoutes.MapScreen.route){
            MapScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
        }


    }
}