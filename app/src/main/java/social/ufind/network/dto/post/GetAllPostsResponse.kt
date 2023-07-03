package social.ufind.network.dto.post

import com.google.gson.annotations.SerializedName
import social.ufind.data.model.PostModel

data class PostsResponse(
    val posts: List<PostModel>,
    val next: Int?,
    val previous: Int
)
data class GetAllPostsResponse(
    val message: PostsResponse,
    val ok: Boolean,
    @SerializedName("errors") val errorMessages: List<String>,
)
