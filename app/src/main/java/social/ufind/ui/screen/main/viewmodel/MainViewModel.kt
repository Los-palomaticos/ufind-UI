package social.ufind.ui.screen.main.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.MainActivity
import social.ufind.navigation.OptionsRoutes
import social.ufind.network.ApiResponse
import social.ufind.repository.UserRepository
import social.ufind.ui.screen.main.MainUiState

class MainViewModel(val repository: UserRepository): ViewModel(), DefaultLifecycleObserver {
    var startDestination = MutableStateFlow(OptionsRoutes.LoadingScreen.route)
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Resume)
    val uiState: StateFlow<MainUiState>
        get() = _uiState

    private val _isUserValid = mutableStateOf(false)
    val isUserValid: State<Boolean>
        get() = _isUserValid
    suspend fun validateToken () {
        when(val response = repository.validateToken()) {
            is ApiResponse.Success -> {
                _uiState.value = MainUiState.Success
            }
            is ApiResponse.ConectionError -> {
                _uiState.value = MainUiState.ConnectionError(response.message)
            }
            else -> {
                _uiState.value = MainUiState.InvalidCredentials
            }
        }
    }
    fun resetState() {
        _uiState.value = MainUiState.Resume
    }
    
    fun updateStartDestination(value: String) {
        startDestination.value = value
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as social.ufind.UfindApplication
                MainViewModel(app.userRepository)
            }
        }
    }
}
