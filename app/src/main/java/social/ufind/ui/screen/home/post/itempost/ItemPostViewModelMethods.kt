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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.network.ApiResponse
import social.ufind.repository.PostRepository
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

interface ItemPostViewModelMethods {
    val itemUiState: StateFlow<ItemUiState>
    fun savePost(post_id: Int)
    fun deleteSavedPost(id: Int)
    fun resetState ()
    fun enviarCorreo(context: Context, destinatario: String, asunto: String)
    fun descargarImagen(context: Context, url: String): File
    fun compartirContenido(context: Context, texto: String, file: File)
}

class ItemPostViewModel(val repository: PostRepository): ViewModel(), ItemPostViewModelMethods {
    private val _itemUiState = MutableStateFlow<ItemUiState>(ItemUiState.Resume)
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
            }
        }
        resetState()
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
            Toast.makeText(context, "No se encontró ninguna aplicación de correo", Toast.LENGTH_SHORT).show()
            Log.d("ItemPostViewModel", e.toString())
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

}