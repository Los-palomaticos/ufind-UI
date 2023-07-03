package social.ufind.ui.screen.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
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
        logOutFireBase()
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

    fun logOutFireBase(){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
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