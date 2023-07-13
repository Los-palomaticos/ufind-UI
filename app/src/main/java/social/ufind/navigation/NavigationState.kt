package social.ufind.navigation

sealed class NavigationState {
    object Idle: NavigationState()
    data class NavigateToRoute(val route: String) : NavigationState()
    data class PopToRoute(val route: String) : NavigationState()
}