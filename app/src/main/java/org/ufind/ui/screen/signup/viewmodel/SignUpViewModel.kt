package org.ufind.ui.screen.signup.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
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

    val errorMessage = mutableStateOf("")
    val isValid = mutableStateOf(false)
    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Resume)
    val uiState:StateFlow<SignUpUiState>
        get() = _uiState

    fun signup(){
        resetState()
        viewModelScope.launch {
            when(val response = repository.signup(username.value, email.value, password.value)) {
                is ApiResponse.Success -> {
                    _uiState.value = SignUpUiState.Success(response.data)
                    clear()
                    resetState()
                    routeNavigator.navigateToRoute(OptionsRoutes.LogIn.route)
                }
                is ApiResponse.ErrorWithMessage -> _uiState.value = SignUpUiState.ErrorWithMessage(response.message)
                is ApiResponse.Error -> _uiState.value = SignUpUiState.Error(response.exception)
            }
        }
    }
    fun getUser() {
        viewModelScope.launch {
             repository.getUserData().collect{
                if(it.token!="") {
                    navigateToRoute(OptionsRoutes.UserInterface.route)
                }
            }
        }
    }
    //        viewModelScope.launch {
//        }
    fun clear() {
        username.value = ""
        email.value = ""
        password.value = ""
        repeatedPassword.value = ""
        isValid.value = false
    }
    private fun resetState () {
        _uiState.value = SignUpUiState.Resume
    }
    fun checkData() {
        isValid.value = (
                username.value != "" &&
                Patterns.EMAIL_ADDRESS.matcher(email.value).matches() &&
                password.value.length >= 6 &&
                repeatedPassword.value == password.value
        )
    }
    fun handleUiStatus(context: Context) {
        when(uiState.value) {
            is SignUpUiState.Resume -> {

            }
            is SignUpUiState.Success -> Toast.makeText(context, "Registro exitoso, ahora puedes iniciar sesion", Toast.LENGTH_LONG).show()
            is SignUpUiState.ErrorWithMessage -> {
                Toast.makeText(context, (uiState.value as SignUpUiState.ErrorWithMessage).message, Toast.LENGTH_LONG).show()
            }
            is SignUpUiState.Error -> {
                Toast.makeText(context, (uiState.value as SignUpUiState.Error).exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun changeErrorMessage() {
        Log.d("APP_TAG", "EEEEEEE")
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