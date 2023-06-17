package org.ufind.network.dto.signup

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    val message: String,
    @SerializedName("errors") val errorMessages: List<String>,
    val ok: Boolean,
)
