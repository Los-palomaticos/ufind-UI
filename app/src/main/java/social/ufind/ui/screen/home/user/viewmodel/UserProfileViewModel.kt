package social.ufind.ui.screen.home.user.viewmodel

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

//Aqui debo agregar  las funciones que ocuparemos


class UserProfileViewModel(val repository: UserRepository, private val routeNavigator: RouteNavigator = UfindNavigator()):
    ViewModel(),
    RouteNavigator by routeNavigator
{
    fun getInformationUser() {
        viewModelScope.launch {
            repository.getInformationUser()
        }
    }

    fun navigateToSettings() {
        routeNavigator.navigateToRoute(OptionsRoutes.SettingsScreen.route)
    }

    fun navigateToWallet() {
        routeNavigator.navigateToRoute(OptionsRoutes.WalletScreen.route)
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as social.ufind.UfindApplication
                UserProfileViewModel(app.userRepository)
            }
        }
    }
}