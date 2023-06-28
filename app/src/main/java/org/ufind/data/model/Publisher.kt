package org.ufind.data.model

data class Publisher(
    val id: Int,
    val username: String,
    val reported: Int,
    val email: String,
    val banned: Boolean
)
