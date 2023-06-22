package org.ufind.ui.screen.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import org.ufind.UfindApplication
import org.ufind.data.OptionsRoutes
import org.ufind.navigation.RouteNavigator
import org.ufind.navigation.UfindNavigator
import org.ufind.repository.UserRepository

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

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                SettingsViewModel(app.userRepository)
            }
        }
    }
}