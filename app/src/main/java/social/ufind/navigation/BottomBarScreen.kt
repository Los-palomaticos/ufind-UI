package social.ufind.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )    object Chat: BottomBarScreen(
        route = "chat",
        title = "Chat",
        icon = Icons.Default.Chat
    )    object SavedPosts: BottomBarScreen(
        route = "savedPosts",
        title = "Guardados",
        icon = Icons.Default.Bookmark
    )    object Profile: BottomBarScreen(
        route = "profile",
        title = "Perfil",
        icon = Icons.Default.Person
    )
}