package org.ufind.data
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.ufind.ui.screen.userhomescreen.HomeMenuScreen
import org.ufind.ui.screen.userhomescreen.MainChatScreen
import org.ufind.ui.screen.userhomescreen.SavedPostScreen
import org.ufind.ui.screen.userhomescreen.UserProfileScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route ){
        composable(route= BottomBarScreen.Home.route){
            HomeMenuScreen()
        }
        composable(route= BottomBarScreen.Profile.route){
            UserProfileScreen()
        }

        composable(route= BottomBarScreen.SavedPosts.route){
            SavedPostScreen()
        }

        composable(route= BottomBarScreen.Chat.route){
            MainChatScreen()
        }
    }
}