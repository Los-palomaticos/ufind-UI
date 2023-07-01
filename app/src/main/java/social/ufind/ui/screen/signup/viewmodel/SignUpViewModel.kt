package social.ufind.ui.screen.signup.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.data.OptionsRoutes
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.repository.UserRepository
import social.ufind.ui.screen.signup.SignUpUiState

class SignUpViewModel(
    val routeNavigator: UfindNavigator = UfindNavigator(),
    val repository: UserRepository,
) : ViewModel(), RouteNavigator by routeNavigator {

    val username = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val repeatedPassword = mutableStateOf("")

    val isValid = mutableStateOf(false)
    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Resume)
    val uiState: StateFlow<SignUpUiState>
        get() = _uiState

    fun signup() {
        createUserInFirebase(email.value, password.value)
        resetState()
        viewModelScope.launch {
            when (val response = repository.signup(username.value, email.value, password.value)) {
                is ApiResponse.Success -> {
                    _uiState.value = SignUpUiState.Success(response.data)
                    clear()
                    resetState()
                    routeNavigator.navigateToRoute(OptionsRoutes.LogIn.route)
                }

                is ApiResponse.ErrorWithMessage -> _uiState.value = response.messages?.let {
                    SignUpUiState.ErrorWithMessage(
                        it
                    )
                }!!

                is ApiResponse.Error -> _uiState.value = SignUpUiState.Error(response.exception)
            }
        }
    }

    fun clear() {
        username.value = ""
        email.value = ""
        password.value = ""
        repeatedPassword.value = ""
        isValid.value = false
    }

    private fun resetState() {
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

    fun navigateToLogin() {
        navigateToRoute(OptionsRoutes.LogIn.route)
    }

    private fun createUserInFirebase(email: String, password: String) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    Log.d(TAG, "Inside_OnCompleteListener")
                    Log.d(TAG, "isSuccessful = ${it.isSuccessful}")
                    val displayName = it.result.user?.email?.split("@")?.get(0)
                    createUserInDataBase(displayName)

                }
                .addOnFailureListener(){
                    Log.d(TAG, "Inside_OnFailureListener")
                    Log.d(TAG, "Exception = ${it.message}")
                    Log.d(TAG, "Exception = ${it.localizedMessage}")
                }
    }

    private fun createUserInDataBase(displayName: String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val user = mutableMapOf<String, Any>()
        user["user_id"] = userId.toString()
        user["display_name"] = displayName.toString()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("BDFire", "Creado ${it.id}")
            }
            .addOnFailureListener{
                Log.d("BDFire", "Ocurrió un error")
            }

    }


    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as social.ufind.UfindApplication
                SignUpViewModel(repository = app.userRepository)
            }
        }
    }
}