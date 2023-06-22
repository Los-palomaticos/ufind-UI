package org.ufind.network

sealed class ApiResponse<T> {
    data class Success<T>(val data: T): ApiResponse<T>()
    data class Error<T>(val exception: Exception) : ApiResponse<T>()
    data class ErrorWithMessage<T>(val messages: List<String>): ApiResponse<T>()
    companion object {
        val connectionErrorMessage = listOf("Error de conexión")
    }
}
