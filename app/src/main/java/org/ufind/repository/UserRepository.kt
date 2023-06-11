package org.ufind.repository

import com.google.gson.Gson
import org.ufind.data.datastore.DataStoreManager
import org.ufind.data.model.Payload
import org.ufind.data.model.UserModel
import org.ufind.network.ApiResponse
import org.ufind.network.dto.signup.SignUpRequest
import org.ufind.network.service.UserService
import org.ufind.utils.JWT
import retrofit2.HttpException
import java.io.IOException
class UserRepository(private val api: UserService, private val dataStoreManager: DataStoreManager) {
//    private lateinit var dataStoreManager: DataStoreManager
    suspend fun signup(username: String, email: String, password: String): ApiResponse<String> {
        return try {
            val response = api.signup(SignUpRequest(username, email, password))
            val jwtDecoded = JWT.decoded(response.token)

            val payload = Gson().fromJson(jwtDecoded, Payload::class.java)

            dataStoreManager.saveUserData(UserModel(
                id = payload.data.id,
                email = payload.data.email,
                username = payload.data.username,
                photo = payload.data.photo,
                token = response.token
            ))

            ApiResponse.Success(response.token)
        } catch(e: HttpException) {
            if (e.code() == 400)
                ApiResponse.ErrorWithMessage("Credenciales inválidas")
            else
                ApiResponse.Error(e)
        } catch(e: IOException) {
            ApiResponse.Error(e)
        }
    }
    fun getUserData() = dataStoreManager.getUserData()
}