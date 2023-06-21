package org.ufind

import android.app.Application
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXConfig
import org.ufind.data.UfindDatabase
import org.ufind.data.datastore.DataStoreManager
import org.ufind.network.retrofit.RetrofitInstance
import org.ufind.repository.PostRepository
import org.ufind.repository.UserRepository

class UfindApplication:Application(), CameraXConfig.Provider {
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
    val postRepository by lazy {
        val service = with(RetrofitInstance) {
            getPostService()
        }
        PostRepository(api = service)
    }
    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setAvailableCamerasLimiter(CameraSelector.DEFAULT_BACK_CAMERA)
            .setMinimumLoggingLevel(Log.ERROR).build()
    }

    companion object {
        private var TOKEN = ""

        fun setToken(token: String) {
            this.TOKEN = token
        }
        fun getToken() = TOKEN
    }
}