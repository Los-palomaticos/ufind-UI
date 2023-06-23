package org.ufind.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.ui.graphics.vector.ImageVector

data class PostModel(
    val id: Int,
    val likes: Int,
    val title: String,
    val description: String,
    val location: String = "Universidad José Simeón Cañas",
    val locationDescription: String,
    val complete: Int,
    val reported: Int,
    val user_id: Int,
    val publisher: Publisher,
    val photos: List<String>
)