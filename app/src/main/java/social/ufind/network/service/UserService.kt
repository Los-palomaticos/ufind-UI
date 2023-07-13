package social.ufind.network.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import social.ufind.network.dto.GeneralResponse
import social.ufind.network.dto.login.LoginRequest
import social.ufind.network.dto.login.LoginResponse
import social.ufind.network.dto.signup.SignUpRequest
import social.ufind.network.dto.signup.SignUpResponse

interface UserService {
    @POST("user/signup")
    suspend fun signup(@Body credentials: SignUpRequest): SignUpResponse
    @POST("user/login")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    @GET("user/getUser")
    suspend fun getInformationUser(): GeneralResponse<String>
    @GET("user/validateToken")
    suspend fun validateToken(): GeneralResponse<String>
}