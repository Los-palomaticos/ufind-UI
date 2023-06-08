package com.example.ufind.data
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ufind.screen.userhomescreen.HomeMenuScreen
import com.example.ufind.screen.userhomescreen.MainChatScreen
import com.example.ufind.screen.userhomescreen.SavedPostScreen
import com.example.ufind.screen.userhomescreen.UserProfileScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination =BottomBarScreen.Home.route ){
        composable(route=BottomBarScreen.Home.route){
            HomeMenuScreen()
        }
        composable(route=BottomBarScreen.Profile.route){
            UserProfileScreen()
        }

        composable(route=BottomBarScreen.SavedPosts.route){
            SavedPostScreen()
        }

        composable(route=BottomBarScreen.Chat.route){
            MainChatScreen()
        }
    }
}