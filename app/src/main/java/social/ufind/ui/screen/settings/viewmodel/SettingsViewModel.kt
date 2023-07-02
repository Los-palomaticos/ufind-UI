package social.ufind.ui.screen.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import social.ufind.navigation.OptionsRoutes
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.repository.UserRepository
import social.ufind.navigation.BottomBarScreen

class SettingsViewModel(val repository: UserRepository, private val routeNavigator: RouteNavigator = UfindNavigator()):
    ViewModel(),
    RouteNavigator by routeNavigator
{
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun navigateToSecurity(){
        routeNavigator.navigateToRoute(OptionsRoutes.SettingsSecurityScreen.route)
    }

    fun navigateToPreferences(){
        routeNavigator.navigateToRoute(OptionsRoutes.SettingsPreferencesScreen.route)
    }

    fun navigateToAccount(){
        routeNavigator.navigateToRoute(OptionsRoutes.SettingsAccountScreen.route)
    }
    fun navigateBack() {
        routeNavigator.navigateToRoute(BottomBarScreen.Profile.route)
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as social.ufind.UfindApplication
                SettingsViewModel(app.userRepository)
            }
        }
    }
}