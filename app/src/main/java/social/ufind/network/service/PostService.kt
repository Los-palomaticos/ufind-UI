package social.ufind.network.service

import androidx.room.Dao
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import social.ufind.data.model.PostModel
import social.ufind.network.dto.GeneralResponse
import social.ufind.network.dto.post.GetAllPostsResponse

@Dao
interface PostService {
    @Multipart
    @POST("post/publish")
    suspend fun addPost(
        @Part photos: List<MultipartBody.Part>,
        @PartMap postData: Map<String, @JvmSuppressWildcards RequestBody>
    ): GeneralResponse<String>

    @GET("post/getAll")
    suspend fun getAll(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): GetAllPostsResponse
}