package org.ufind.network.service

import org.ufind.network.dto.signup.SignUpRequest
import org.ufind.network.dto.signup.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("user/signup")
    suspend fun signup(@Body credentials: SignUpRequest): SignUpResponse
}