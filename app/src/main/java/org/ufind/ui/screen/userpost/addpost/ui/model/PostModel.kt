package org.ufind.ui.screen.userpost.addpost.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.ui.graphics.vector.ImageVector

data class PostModel(
    val title: String,
    val description: String,
    val icon: ImageVector = Icons.Filled.AddAPhoto,
    val location: String = "Universidad José Simeón Cañas",
    val id: Long = System.currentTimeMillis()
)