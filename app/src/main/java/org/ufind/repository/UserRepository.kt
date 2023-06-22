package org.ufind.repository

import android.util.Log
import com.google.gson.Gson
import org.ufind.data.datastore.DataStoreManager
import org.ufind.data.model.Payload
import org.ufind.data.model.UserModel
import org.ufind.network.ApiResponse
import org.ufind.network.dto.login.LoginRequest
import org.ufind.network.dto.login.LoginResponse
import org.ufind.network.dto.signup.SignUpRequest
import org.ufind.network.dto.signup.SignUpResponse
import org.ufind.network.service.UserService
import org.ufind.utils.JWT
import org.ufind.utils.SerializeErrorBody
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException

class UserRepository(private val api: UserService, private val dataStoreManager: DataStoreManager) {
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
            if (response.ok)
                ApiResponse.Success(response.message)
            else
                ApiResponse.ErrorWithMessage(response.errorMessages)
        } catch (e: ConnectException) {
                ApiResponse.ErrorWithMessage(listOf("Error de conexión"))
        } catch(e: HttpException) {
            val errorResponse = SerializeErrorBody.getSerializedError(e, SignUpResponse::class.java)
            ApiResponse.ErrorWithMessage(errorResponse.errorMessages)
        }  catch(e: IOException) {
            ApiResponse.Error(e)
        }
    }
    suspend fun login(loginRequest: LoginRequest): ApiResponse<String> {
        return try {
            val response = api.login(loginRequest)
            if (response.ok) {
                saveUserToDataStore(response.token)
                ApiResponse.Success("Inicio de sesión exitoso")
            } else {
                ApiResponse.ErrorWithMessage(response.errorMessages)
            }
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch(e: HttpException) {
            val errorResponse = SerializeErrorBody.getSerializedError(e, LoginResponse::class.java)

            ApiResponse.ErrorWithMessage(errorResponse.errorMessages)
        } catch(e:IOException) {
            ApiResponse.Error(e)
        }
    }
    fun getUserData() = dataStoreManager.getUserData()

}