package org.ufind.ui.screen.login

sealed class LoginUiState {
    object Resume: LoginUiState()
    data class Success(val token: String): LoginUiState()
    data class ErrorWithMessage(val message: String): LoginUiState()
    data class Error(val e: Exception): LoginUiState()
}
