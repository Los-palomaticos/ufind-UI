package social.ufind.ui.screen.home.post.itempost

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.UfindApplication
import social.ufind.navigation.BottomBarScreen
import social.ufind.navigation.OptionsRoutes
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.repository.PostRepository
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

interface ItemPostViewModelMethods {
    val itemUiState: StateFlow<ItemUiState>
    val _isOptionsExpanded: MutableStateFlow<Boolean>
    val isOptionsExpanded: StateFlow<Boolean>
        get() = _isOptionsExpanded
    fun savePost(post_id: Int)
    fun deleteSavedPost(id: Int)
    fun resetState ()
    fun deletePost(id: Int)
    fun reportPost(id: Int)
    fun isOwner(publisherId: Int): Boolean
    fun enviarCorreo(context: Context, destinatario: String, asunto: String)
    fun descargarImagen(context: Context, url: String): File
    fun compartirContenido(context: Context, texto: String, file: File)
    fun navigateToChat(userJson: String)
}

class ItemPostViewModel(
    val repository: PostRepository,
): ViewModel(), ItemPostViewModelMethods{
    private val routeNavigator: UfindNavigator = UfindNavigator()

    private val userId = UfindApplication.getUserId()
    private val _itemUiState = MutableStateFlow<ItemUiState>(ItemUiState.Resume)
    override val _isOptionsExpanded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val itemUiState: StateFlow<ItemUiState>
        get() = _itemUiState
    override fun resetState () {
        _itemUiState.value = ItemUiState.Resume
    }
    override fun savePost(post_id: Int) {
        viewModelScope.launch {
            when(val response = repository.savePost(post_id)){
                is ApiResponse.Success -> {
                    _itemUiState.value = ItemUiState.Success(response.data)
                }
                is ApiResponse.ErrorWithMessage -> {
                    _itemUiState.value = ItemUiState.ErrorWithMessage(response.messages)
                }
                is ApiResponse.Error -> {
                    _itemUiState.value = ItemUiState.Error(response.exception)
                }

                else -> {}
            }
        }
        resetState()
    }
    override fun isOwner(publisherId: Int): Boolean {
        return (userId == publisherId)
    }
    override fun deletePost(id: Int) {
        viewModelScope.launch {
            when (val response = repository.deletePost(id)) {
                is ApiResponse.Success -> {
                    _itemUiState.value = ItemUiState.Success(response.data)
                }
                is ApiResponse.ErrorWithMessage -> {
                    _itemUiState.value = ItemUiState.ErrorWithMessage(response.messages)
                }
                is ApiResponse.Error -> {
                    _itemUiState.value = ItemUiState.Error(response.exception)
                }

                else -> {}
            }
        }
    }

    override fun reportPost(id: Int) {
        viewModelScope.launch {
            when (val response = repository.reportPost(id)) {
                is ApiResponse.Success -> {
                    _itemUiState.value = ItemUiState.Success(response.data)
                }
                is ApiResponse.ErrorWithMessage -> {
                    _itemUiState.value = ItemUiState.ErrorWithMessage(response.messages)
                }
                is ApiResponse.Error -> {
                    _itemUiState.value = ItemUiState.Error(response.exception)
                }

                else -> {}
            }
        }
    }
    override fun deleteSavedPost(id: Int) {
        viewModelScope.launch {
            when(val response =  repository.deleteSavedPost(id)) {
                is ApiResponse.Success -> {
                    _itemUiState.value = ItemUiState.Success(response.data)
                }
                is ApiResponse.ErrorWithMessage -> {
                    _itemUiState.value = ItemUiState.ErrorWithMessage(response.messages)
                }
                is ApiResponse.Error -> {
                    _itemUiState.value = ItemUiState.Error(response.exception)
                }

                else -> {}
            }
        }
        resetState()
    }

    override fun enviarCorreo(context: Context, destinatario: String, asunto: String) {
        try {
            val emailSelectorIntent = Intent(Intent.ACTION_SENDTO)
            emailSelectorIntent.data = Uri.parse("mailto:")

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(destinatario))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto)
            emailIntent.selector = emailSelectorIntent

            context.startActivity(emailIntent)
        } catch (e: Exception) {
            try {
                enviarCorreoCasoFalla(context, destinatario, asunto)
            } catch(e: Exception) {
                Toast.makeText(context, "No se encontró ninguna aplicación de correo", Toast.LENGTH_SHORT).show()
                Log.d("ItemPostViewModel", e.toString())
            }
        }
    }

    override fun compartirContenido(context: Context, texto: String, file: File) {
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
                "No se encontraron aplicaciones para compartir",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun descargarImagen(context: Context, url: String): File {
        val fileName = "imagen.jpg" // Puedes cambiar el nombre del archivo si lo deseas
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

    override fun navigateToChat(userJson: String) {
        Log.d("APP_TAG", "hola")
//        routeNavigator.navigateToRoute(BottomBarScreen.SavedPosts.route)
//        routeNavigator.navigateToRoute(
//            OptionsRoutes.ChatScreen2.route
//            .replace(
//                oldValue = "{user}",
//                newValue = userJson
//            )
//        )
    }

    fun enviarCorreoCasoFalla(context: Context, destinatario: String, asunto: String) {
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
}