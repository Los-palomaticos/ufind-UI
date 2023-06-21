package org.ufind.network.dto

import com.google.gson.annotations.SerializedName

data class GeneralResponse (
    val message: String,
    val ok: Boolean,
    @SerializedName("errors") val errorMessages: List<String>,
)