package social.ufind.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import social.ufind.UfindApplication
import social.ufind.data.UfindDatabase
import social.ufind.data.datastore.DataStoreManager
import social.ufind.data.mediator.PostMediator
import social.ufind.data.mediator.SavedPostsMediator
import social.ufind.data.mediator.UserPostsMediator
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.network.ApiResponse
import social.ufind.network.dto.GeneralResponse
import social.ufind.network.dto.post.PostOptionsRequest
import social.ufind.network.dto.post.SavePostRequest
import social.ufind.network.service.PostService
import social.ufind.utils.SerializeErrorBody
import java.io.File
import java.io.IOException
import java.net.ConnectException

class PostRepository(
    private val database: UfindDatabase,
    private val api: PostService,
    private val dataStoreManager: DataStoreManager
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
    suspend fun deletePost(id: Int): ApiResponse<String> {
        return try {
            val response = api.deletePost(PostOptionsRequest(id))
            ApiResponse.Success(response.message)
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch (e: HttpException) {
            val errorBody = SerializeErrorBody.getSerializedError(e, GeneralResponse::class.java)
            ApiResponse.ErrorWithMessage(errorBody.errorMessages)
        } catch (e: Exception) {
            Log.d("APP_TAG", e.message.toString())
            ApiResponse.Error(e)
        }
    }
    suspend fun reportPost(id: Int): ApiResponse<String> {
        return try {
            val response = api.reportPost(PostOptionsRequest(id))
            ApiResponse.Success(response.message)
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch (e: HttpException) {
            Log.d("APP_TAG", e.code().toString())
            Log.d("APP_TAG", e.message())
            if (e.code() == 400) {
                ApiResponse.ErrorWithMessage<String>(listOf("Error interno"))
            }
            val errorBody = SerializeErrorBody.getSerializedError(e, GeneralResponse::class.java)
            ApiResponse.ErrorWithMessage(errorBody.errorMessages)
        } catch (e: Exception) {
            Log.d("APP_TAG", e.message.toString())
            ApiResponse.Error(e)
        }
    }
    @OptIn(ExperimentalPagingApi::class)
    fun getAll(size: Int): ApiResponse<Flow<PagingData<PostWithAuthorAndPhotos>>> {
        return try {
            val flow = Pager(
                config= PagingConfig(
                    pageSize = size,
                    prefetchDistance = (0.2*size).toInt()
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
    @OptIn(ExperimentalPagingApi::class)
    fun getSavedPosts(size: Int): ApiResponse<Flow<PagingData<PostWithAuthorAndPhotos>>> {
        return try {
            val flow = Pager(
                config = PagingConfig(
                    pageSize = size,
                    prefetchDistance = (0.2*size).toInt()
                ),
                remoteMediator = SavedPostsMediator(database, api)
            ) {
                postDao.getSavedPosts()
            }.flow
            ApiResponse.Success(flow)
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        }
    }
    @OptIn(ExperimentalPagingApi::class)
    fun getUserPosts(size: Int) : ApiResponse<Flow<PagingData<PostWithAuthorAndPhotos>>> {
        return try {
            val flow = Pager(
                config = PagingConfig(
                    pageSize = size,
                    prefetchDistance = (0.2*size).toInt()
                ),
                remoteMediator = UserPostsMediator(database, api)
            ){
                postDao.getUserPosts(UfindApplication.getUserId())
            }.flow
            ApiResponse.Success(flow)
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        }
    }
    suspend fun savePost(id: Int): ApiResponse<String> {
        return try {
            val response = api.savePost(SavePostRequest(id))
            ApiResponse.Success(response.message)
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    suspend fun deleteSavedPost(id: Int): ApiResponse<String> {
        return try {
            val response = api.deleteSavedPost(SavePostRequest(id))
            ApiResponse.Success(response.message)
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    suspend fun setPostTutorialTrue() {
        dataStoreManager.setPostTutorialTrue()
    }
    fun getPostTutorial(): Flow<String> {
        return dataStoreManager.getPostTutorial()
    }
}