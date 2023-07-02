package social.ufind.repository

import android.R
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import social.ufind.data.UfindDatabase
import social.ufind.data.mediator.PostMediator
import social.ufind.data.model.PostModel
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.network.ApiResponse
import social.ufind.network.dto.GeneralResponse
import social.ufind.network.service.PostService
import social.ufind.utils.SerializeErrorBody
import java.io.File
import java.io.IOException
import java.net.ConnectException

class PostRepository(
    private val database: UfindDatabase,
    private val api: PostService
) {
    private val postDao = database.postDao()
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
            postData["location"] = getRequestMultipartBody(location ?: "")
            val response = api.addPost(
                photos = photos,
                postData = postData
            )
            ApiResponse.Success(response.message)
        } catch (e: ConnectException){
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch (e: HttpException) {
            if (e.code() == 413) {
                ApiResponse.ErrorWithMessage(listOf("El tamaño de la imagen es muy grande"))
            } else {
                val errorBody = SerializeErrorBody.getSerializedError(e, GeneralResponse::class.java)
                ApiResponse.ErrorWithMessage(errorBody.errorMessages)
            }
        } catch (e: IOException) {
            ApiResponse.Error(e)
        } finally {
            photos.clear()
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAll(size: Int): ApiResponse<Flow<PagingData<PostWithAuthorAndPhotos>>> {
        return try {
            val flow = Pager(
                config= PagingConfig(
                    pageSize = size,
                    prefetchDistance = 2
                ),
                remoteMediator = PostMediator(database, api)
            ) {
                postDao.getAll()
            }.flow
            ApiResponse.Success(flow)
        } catch (e: ConnectException){
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch(e: HttpException) {
            val errorResponse = SerializeErrorBody.getSerializedError(e, GeneralResponse::class.java)
            ApiResponse.ErrorWithMessage(errorResponse.errorMessages)
        } catch (e: IOException) {
            ApiResponse.Error(e)
        }
    }
}