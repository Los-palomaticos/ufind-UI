package social.ufind.network.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import social.ufind.UfindApplication.Companion.getToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import social.ufind.network.service.PostService
import social.ufind.network.service.UserService

const val BASE_URL = "https://ufind.social/api/"
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