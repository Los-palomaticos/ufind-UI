package org.ufind.network.service

import org.ufind.network.dto.GeneralResponse
import org.ufind.network.dto.login.LoginRequest
import org.ufind.network.dto.login.LoginResponse
import org.ufind.network.dto.signup.SignUpRequest
import org.ufind.network.dto.signup.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("user/signup")
    suspend fun signup(@Body credentials: SignUpRequest): SignUpResponse
    @POST("user/login")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    @GET("user/validateToken")
    suspend fun validateToken(): GeneralResponse<String>
}