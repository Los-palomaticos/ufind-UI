package social.ufind.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface RouteNavigator {
    fun onNavigated(state: NavigationState)
    fun navigateToRoute(route: String)
    fun popToRoute(route: String)
    val navigationState: StateFlow<NavigationState>
}

final class UfindNavigator: RouteNavigator {
    override val navigationState: MutableStateFlow<NavigationState> = MutableStateFlow(NavigationState.Idle)

    override fun popToRoute(route: String) {
        navigate(NavigationState.PopToRoute(route))
    }

    override fun navigateToRoute(route: String) {
        navigate(NavigationState.NavigateToRoute(route))
    }

    override fun onNavigated(state: NavigationState) {
        navigationState.compareAndSet(state, NavigationState.Idle)
    }
    final fun navigate(state: NavigationState) {
        navigationState.value = state
    }
}
