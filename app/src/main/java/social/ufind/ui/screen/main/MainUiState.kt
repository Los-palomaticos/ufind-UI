package social.ufind.ui.screen.main

sealed class MainUiState {
    object Resume: MainUiState()
    object Success: MainUiState()
    data class ConnectionError(val message: String): MainUiState()
    object InvalidCredentials: MainUiState()
}