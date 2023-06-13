package org.ufind

import android.app.Application
import org.ufind.data.UfindDatabase
import org.ufind.data.datastore.DataStoreManager
import org.ufind.network.retrofit.RetrofitInstance
import org.ufind.repository.UserRepository

class UfindApplication:Application() {
    private val database by lazy {
        UfindDatabase.getInstance(this)
    }
    val  dataStoreManager by lazy {
        DataStoreManager(this)
    }
    val userRepository by lazy {
//        val userLocalDatabase = database.userDao()
        val service = with(RetrofitInstance)  {
            getAuthService()
        }
        UserRepository(api = service, dataStoreManager = dataStoreManager)
    }
}