package social.ufind.ui.screen.home.post.add

sealed class AddPostUiState {
    object Resume: AddPostUiState()
    object Sending: AddPostUiState()
    data class Success(val message: String): AddPostUiState()
    data class ErrorWithMessage(val errorMessages: List<String>): AddPostUiState()
    data class Error(val e: Exception): AddPostUiState()
}
