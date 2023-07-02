package social.ufind.ui.screen.login.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.network.dto.login.LoginRequest
import social.ufind.repository.UserRepository
import social.ufind.ui.screen.login.LoginUiState

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
        signInFirebase(email.value, password.value)
        resetState()
        viewModelScope.launch {
            when (val response = repository.login(LoginRequest(email.value, password.value))) {
                is ApiResponse.Success -> {
                    _uiState.value = LoginUiState.Success(response.data)
                    clear()
                    resetState()
                }

                is ApiResponse.ErrorWithMessage -> _uiState.value =
                    LoginUiState.ErrorWithMessage(response.messages)

                is ApiResponse.Error -> _uiState.value = LoginUiState.Error(response.exception)
            }
        }
    }

    private fun signInFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignIn", "signInWithEmailAndPassword is successful")
                } else {
                    Log.d("SignInError", "signInWithEmailAndPassword: ${task.result.toString()}")
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
                val app = this[APPLICATION_KEY] as social.ufind.UfindApplication
                LoginViewModel(repository = app.userRepository)
            }

        }
    }
}