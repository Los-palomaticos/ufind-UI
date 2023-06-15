package org.ufind.ui.screen.login.viewmodel

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ufind.UfindApplication
import org.ufind.navigation.RouteNavigator
import org.ufind.navigation.UfindNavigator
import org.ufind.network.ApiResponse
import org.ufind.network.dto.login.LoginRequest
import org.ufind.repository.UserRepository
import org.ufind.ui.screen.login.LoginUiState

class LoginViewModel(
    private val routeNavigator: UfindNavigator = UfindNavigator(),
    val repository: UserRepository
) : ViewModel(), RouteNavigator by routeNavigator {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val isValid = mutableStateOf(false)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Resume)
    val uiState: StateFlow<LoginUiState>
        get() = _uiState

    fun login() {
        viewModelScope.launch {
            when (val response = repository.login(LoginRequest(email.value, password.value))) {
                is ApiResponse.Success -> {
                    _uiState.value = LoginUiState.Success(response.data)
                    clear()
                    resetState()

                }

                is ApiResponse.ErrorWithMessage -> _uiState.value =
                    LoginUiState.ErrorWithMessage(response.message)

                is ApiResponse.Error -> _uiState.value = LoginUiState.Error(response.exception)
            }
        }
    }

    fun checkData() {
        isValid.value = (
                Patterns.EMAIL_ADDRESS.matcher(email.value).matches() &&
                        password.value.length >= 6
                )
    }

    fun clear() {
        email.value = ""
        password.value = ""
    }

    private fun resetState() {
        _uiState.value = LoginUiState.Resume
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                LoginViewModel(repository = app.userRepository)
            }

        }
    }
}