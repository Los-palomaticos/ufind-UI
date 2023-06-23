package org.ufind.network.service

import androidx.room.Dao
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ufind.data.model.PostModel
import org.ufind.network.dto.GeneralResponse
import org.ufind.network.dto.post.AddPostRequest
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

@Dao
interface PostService {
    @Multipart
    @POST("post/publish")
    suspend fun addPost(
        @Part photos: List<MultipartBody.Part>,
        @PartMap postData: Map<String, @JvmSuppressWildcards RequestBody>
    ): GeneralResponse<String>

    @GET("post/getAll")
    suspend fun getAll(): GeneralResponse<List<PostModel>>
}