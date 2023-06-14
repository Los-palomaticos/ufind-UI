package org.ufind.repository

import com.google.gson.Gson
import org.ufind.data.datastore.DataStoreManager
import org.ufind.data.model.Payload
import org.ufind.data.model.UserModel
import org.ufind.network.ApiResponse
import org.ufind.network.dto.login.LoginRequest
import org.ufind.network.dto.signup.SignUpRequest
import org.ufind.network.service.UserService
import org.ufind.utils.JWT
import retrofit2.HttpException
import java.io.IOException
class UserRepository(private val api: UserService, private val dataStoreManager: DataStoreManager) {
//    private lateinit var dataStoreManager: DataStoreManager

    private suspend fun saveUserToDataStore(token: String) {
        val jwtDecoded = JWT.decoded(token)

        val payload = Gson().fromJson(jwtDecoded, Payload::class.java)

        dataStoreManager.saveUserData(UserModel(
            id = payload.data.id,
            email = payload.data.email,
            username = payload.data.username,
            photo = payload.data.photo,
            token = token
        ))
    }
    suspend fun signup(username: String, email: String, password: String): ApiResponse<String> {
        return try {
            val response = api.signup(SignUpRequest(username, email, password))
            ApiResponse.Success("Registro exitoso")
        } catch(e: HttpException) {
            if (e.code() == 400)
                ApiResponse.ErrorWithMessage("Credenciales inválidas")
            else
                ApiResponse.Error(e)
        } catch(e: IOException) {
            ApiResponse.Error(e)
        }
    }
    suspend fun login(loginRequest: LoginRequest): ApiResponse<String> {
        try {
            val response = api.login(loginRequest)
            saveUserToDataStore(response.token)

            return ApiResponse.Success("Inicio de sesion exitoso")
        } catch(e: HttpException) {
            if(e.code() == 401) {
                return ApiResponse.ErrorWithMessage("Credenciales invalidas")
            }
            return ApiResponse.ErrorWithMessage("Error de servidor")
        } catch(e:IOException) {
            return ApiResponse.Error(e)
        }
    }
    fun getUserData() = dataStoreManager.getUserData()

}