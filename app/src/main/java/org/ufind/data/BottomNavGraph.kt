package org.ufind.data
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.ufind.ui.screen.home.MainChatScreen
import org.ufind.ui.screen.home.SavedPostScreen
import org.ufind.ui.screen.home.UserProfileScreen
import org.ufind.ui.screen.home.post.add.camera.CameraScreen
import org.ufind.ui.screen.userpost.addpost.ui.AddPostScreen
import org.ufind.ui.screen.userpost.addpost.ui.PostScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route ){
        composable(route= BottomBarScreen.Home.route){
            PostScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
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

        AddPostScreen.composable(this, navController)
        CameraScreen.composable(this, navController)

        composable(route = OptionsRoutes.PostScreen.route) {
            PostScreen { navController.navigate(OptionsRoutes.AddPostScreen.route) }
        }

    }
}