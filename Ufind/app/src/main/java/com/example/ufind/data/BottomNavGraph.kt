package com.example.ufind.data
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ufind.screen.HomeMenuScreen
import com.example.ufind.screen.MainChatScreen
import com.example.ufind.screen.SavedPostScreen
import com.example.ufind.screen.UserProfileScreen


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