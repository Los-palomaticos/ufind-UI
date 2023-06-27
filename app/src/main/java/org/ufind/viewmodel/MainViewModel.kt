package org.ufind.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.ufind.UfindApplication
import org.ufind.data.OptionsRoutes
import org.ufind.data.datastore.DataStoreManager
import org.ufind.network.ApiResponse
import org.ufind.repository.UserRepository

class MainViewModel(val repository: UserRepository): ViewModel() {
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
                val app = this[APPLICATION_KEY] as UfindApplication
                MainViewModel(app.userRepository)
            }
        }
    }
}
