package social.ufind.viewmodel

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
import kotlinx.coroutines.launch
import social.ufind.MainActivity
import social.ufind.navigation.OptionsRoutes
import social.ufind.network.ApiResponse
import social.ufind.repository.UserRepository

class MainViewModel(val repository: UserRepository): ViewModel(), DefaultLifecycleObserver {
    var startDestination = MutableStateFlow(OptionsRoutes.LogInOrSignUpOptions.route)
    private val _isUserValid = mutableStateOf(false)
    val isUserValid: State<Boolean>
        get() = _isUserValid
    suspend fun validateToken () {
        when(val response = repository.validateToken()) {
            is ApiResponse.Success -> {
                _isUserValid.value = response.data
            } else -> {
                _isUserValid.value = false
            }
        }
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
