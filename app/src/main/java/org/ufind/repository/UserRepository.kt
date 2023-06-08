package org.ufind.repository

import android.util.Log
import org.ufind.network.ApiResponse
import org.ufind.network.dto.signup.SignUpRequest
import org.ufind.network.service.UserService
import retrofit2.HttpException
import java.io.IOException

class UserRepository(private val api: UserService) {
    suspend fun signup(username: String, email: String, password: String): ApiResponse<String> {
        return try {
            val response = api.signup(SignUpRequest(username, email, password))
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
}