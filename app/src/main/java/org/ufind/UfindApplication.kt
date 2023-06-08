package org.ufind

import android.app.Application
import org.ufind.network.retrofit.RetrofitInstance
import org.ufind.repository.UserRepository

class UfindApplication:Application() {
    val userRepository by lazy {
        val service = with(RetrofitInstance)  {
            getAuthService()
        }
        UserRepository(service)
    }
}