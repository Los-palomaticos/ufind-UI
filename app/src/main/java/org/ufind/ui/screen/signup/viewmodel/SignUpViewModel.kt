package org.ufind.ui.screen.signup.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.ufind.UfindApplication
import org.ufind.data.OptionsRoutes
import org.ufind.navigation.RouteNavigator
import org.ufind.navigation.UfindNavigator
import org.ufind.network.ApiResponse
import org.ufind.repository.UserRepository
import org.ufind.ui.screen.signup.SignUpUiState
class SignUpViewModel(
        val routeNavigator: UfindNavigator = UfindNavigator(),
        val repository: UserRepository,
    ): ViewModel(), RouteNavigator by routeNavigator {

    val username = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val repeatedPassword = mutableStateOf("")
    val isValid = mutableStateOf(false)
    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Resume)
    val uiState
        get() = _uiState

//    val navigationState: MutableStateFlow<NavigationState> = MutableStateFlow(NavigationState.Idle)

    fun signup(){
        viewModelScope.launch {
            val response = repository.signup(username.value, email.value, password.value)
            when(response) {
                is ApiResponse.Success -> {
                    _uiState.value = SignUpUiState.Success(response.data)
                    clear()
                    routeNavigator.navigateToRoute(OptionsRoutes.UserInterface.route)
                }
                is ApiResponse.ErrorWithMessage -> _uiState.value = SignUpUiState.ErrorWithMessage(response.message)
                is ApiResponse.Error -> _uiState.value = SignUpUiState.Error(response.exception)
            }
        }
    }

    fun clear() {
        username.value = ""
        password.value = ""
        repeatedPassword.value = ""
        isValid.value = false
    }
    fun checkData() {
        isValid.value = (
                username.value != "" &&
                email.value != "" &&
                password.value != "" &&
                repeatedPassword.value == password.value
        )
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                SignUpViewModel(repository = app.userRepository)
            }
        }
    }
}