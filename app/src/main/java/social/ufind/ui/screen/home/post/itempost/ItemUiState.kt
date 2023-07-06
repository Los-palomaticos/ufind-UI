package social.ufind.ui.screen.home.post.itempost

sealed class ItemUiState {
    object Resume: ItemUiState()
    data class Success(val message: String): ItemUiState()
    data class ErrorWithMessage(val errorMessages: List<String>): ItemUiState()
    data class Error(val e: Exception): ItemUiState()
}