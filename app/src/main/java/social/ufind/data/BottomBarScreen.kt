package social.ufind.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: social.ufind.data.BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )    object Chat: social.ufind.data.BottomBarScreen(
        route = "ContactsScreen",
        title = "Chat",
        icon = Icons.Default.Chat
    )    object SavedPosts: social.ufind.data.BottomBarScreen(
        route = "savedPosts",
        title = "Guardados",
        icon = Icons.Default.Bookmark
    )    object Profile: social.ufind.data.BottomBarScreen(
        route = "profile",
        title = "Perfil",
        icon = Icons.Default.Person
    )
}