package org.ufind.network.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message") val token: String,
    @SerializedName("errors") val errorMessages: List<String>,
    val ok: Boolean,
)
