package org.ufind.network.dto.login

data class LoginRequest (
    val email: String,
    val password: String,
)