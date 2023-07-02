package social.ufind

import android.app.Application
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXConfig
import social.ufind.data.UfindDatabase
import social.ufind.data.datastore.DataStoreManager
import social.ufind.network.retrofit.RetrofitInstance
import social.ufind.repository.PostRepository
import social.ufind.repository.UserRepository

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
        PostRepository(api = service, database = database)
    }
    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setAvailableCamerasLimiter(CameraSelector.DEFAULT_BACK_CAMERA)
            .setMinimumLoggingLevel(Log.ERROR).build()
    }

    companion object {
        private var TOKEN = ""

        fun setToken(token: String) {
            social.ufind.UfindApplication.Companion.TOKEN = token
        }
        fun getToken() = social.ufind.UfindApplication.Companion.TOKEN
    }
}