package social.ufind.network.dto.post

import okhttp3.MultipartBody
import java.io.File
import java.util.Properties
data class AddPostRequest (
    val title: String,
    val description: String,

    val location: String? = null,
)