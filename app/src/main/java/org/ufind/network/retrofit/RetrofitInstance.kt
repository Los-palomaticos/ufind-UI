package org.ufind.network.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.ufind.UfindApplication.Companion.getToken
import org.ufind.network.service.PostService
import org.ufind.network.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://34.174.97.231:3000/api/"
object RetrofitInstance {
    private val client = OkHttpClient.Builder().apply{
        addInterceptor(
            Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${getToken()}")
                    .build()
                chain.proceed(newRequest)
            }
        )
    }.build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getAuthService() = retrofit.create(UserService::class.java)
    fun getPostService() = retrofit.create(PostService::class.java)
}