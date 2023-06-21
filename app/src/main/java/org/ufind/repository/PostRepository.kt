package org.ufind.repository

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.ufind.network.ApiResponse
import org.ufind.network.dto.GeneralResponse
import org.ufind.network.service.PostService
import org.ufind.utils.SerializeErrorBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class PostRepository(private val api: PostService) {
    private val photos = mutableListOf<MultipartBody.Part>()

    private fun setPhotos(file: File) {
        val requestPhoto = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        photos.add(MultipartBody.Part.createFormData("photos", file.name, requestPhoto))
    }
    private fun getRequestMultipartBody(data: String): RequestBody {
        return data.toRequestBody(MultipartBody.FORM)
    }
    suspend fun addPost(title: String, description: String, photo: File, location: String?=null): ApiResponse<String>{
        return try {
            setPhotos(photo)
            val postData: HashMap<String, RequestBody> = HashMap()
            postData["title"] = getRequestMultipartBody(title)
            postData["description"] = getRequestMultipartBody(description)
            postData["location"] = getRequestMultipartBody(location?:"")
            val response = api.addPost(
                photos = photos,
                postData = postData
            )
            ApiResponse.Success(response.message)
        } catch (e: HttpException) {
            val errorBody = SerializeErrorBody.getSerializedError(e, GeneralResponse::class.java)
            ApiResponse.ErrorWithMessage(errorBody.errorMessages)
        } catch (e: IOException) {
            ApiResponse.Error(e)
        } finally {
            photos.clear()
        }

    }
}