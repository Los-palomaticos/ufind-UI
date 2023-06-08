package org.ufind.ui.screen.signup

sealed class SignUpUiState {
    object Resume: SignUpUiState()
    data class Success(val token: String): SignUpUiState()
    data class ErrorWithMessage(val message: String): SignUpUiState()
    data class Error(val exception: Exception): SignUpUiState()
}