package social.ufind.ui.screen.login

sealed class LoginUiState {
    object Resume: LoginUiState()
    object Sending: LoginUiState()
    data class Success(val token: String): LoginUiState()
    data class ErrorWithMessage(val messages: List<String>): LoginUiState()
    data class Error(val e: Exception): LoginUiState()
}
