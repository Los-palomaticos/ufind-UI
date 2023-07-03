package social.ufind.ui.screen.home.post.viewmodel


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import social.ufind.UfindApplication
import social.ufind.navigation.OptionsRoutes
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.repository.PostRepository
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class PostViewModel(
    private val repository: PostRepository,
    private val routeNavigator: RouteNavigator = UfindNavigator()
): ViewModel(), RouteNavigator by routeNavigator, DefaultLifecycleObserver {
    private var _posts = MutableStateFlow<PagingData<PostWithAuthorAndPhotos>>(PagingData.empty())
    val listOfPosts: Flow<PagingData<PostWithAuthorAndPhotos>>
        get() = _posts
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d("APP_TAG", "CREATED")
    }

    fun refresh() {
        Log.d("APP_TAG", "Refrescando")
        getAll()
    }
    private fun getAll() {
        viewModelScope.launch {
            when(val response = repository.getAll(10)) {
                is ApiResponse.Success -> {
                    response.data.cachedIn(viewModelScope).collect{
                        _posts.value = it
                    }
                }
                is ApiResponse.ErrorWithMessage -> {
                    Log.d("APP_TAG", response.messages.toString())
                }
                is ApiResponse.Error -> {
                    Log.d("APP_TAG", response.exception.toString())
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermissions(context: Context, launcher: ManagedActivityResultLauncher<String, Boolean>) {
        val permission = android.Manifest.permission.CAMERA
        if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            navigateToAddPost()
        else
            launcher.launch(permission)
    }
    fun navigateToAddPost() {
        routeNavigator.navigateToRoute(OptionsRoutes.AddPostScreen.route)
    }

    fun descargarImagen(context: Context, url: String): File {
        val fileName = "imagen_compartida.jpg" // Puedes cambiar el nombre del archivo si lo deseas
        val file = File(context.cacheDir, fileName)

        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        connection.connect()

        val inputStream = connection.inputStream
        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var bytesRead: Int

        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.close()
        inputStream.close()
        return file
    }
    fun compartirContenido(context: Context, texto: String, file: File) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"

        val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        intent.putExtra(Intent.EXTRA_TEXT, texto)

        val shareIntent = Intent.createChooser(intent, "Compartir a través de")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            context.startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "No se encontraron aplicaciones de compartir disponibles",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun enviarCorreo(context: Context, destinatario: String, asunto: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(destinatario))
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto)

        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)

        if (activities.isNotEmpty()) {
            val emailApps = ArrayList<ResolveInfo>()
            for (resolveInfo in activities) {
                val packageName = resolveInfo.activityInfo.packageName
                if (packageName != null && packageName.contains("com.google.android.gm")) {
                    emailApps.add(resolveInfo)
                }
            }

            if (emailApps.isNotEmpty()) {
                intent.`package` = "com.google.android.gm" // Establecer el paquete de Gmail
                context.startActivity(intent)
            } else {
                // Abrir el selector de aplicaciones de correo electrónico
                val chooserIntent = Intent.createChooser(intent, "Seleccionar aplicación de correo")
                if (chooserIntent.resolveActivity(packageManager) != null) {
                    context.startActivity(chooserIntent)
                } else {
                    Toast.makeText(context, "No se encontró ninguna aplicación de correo", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "No se encontró ninguna aplicación de correo", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                PostViewModel(app.postRepository)
            }
        }
    }
}