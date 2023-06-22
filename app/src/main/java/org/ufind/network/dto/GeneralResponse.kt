package org.ufind.network.dto

import com.google.gson.annotations.SerializedName

data class GeneralResponse<T> (
    val message: T,
    val ok: Boolean,
    @SerializedName("errors") val errorMessages: List<String>,
)