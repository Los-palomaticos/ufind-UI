package org.ufind.ui.screen.home.post.add.viewmodel

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ufind.MainActivity
import org.ufind.R
import org.ufind.UfindApplication
import org.ufind.data.OptionsRoutes
import org.ufind.data.datastore.DataStoreManager
import org.ufind.navigation.RouteNavigator
import org.ufind.navigation.UfindNavigator
import org.ufind.network.ApiResponse
import org.ufind.repository.PostRepository
import org.ufind.ui.screen.home.post.add.AddPostUiState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors


class AddPostViewModel(
    val repository: PostRepository,
    val dataStoreManager: DataStoreManager,
    val routeNavigator: UfindNavigator = UfindNavigator()
): ViewModel(), RouteNavigator by routeNavigator, DefaultLifecycleObserver {
    // variables de estado
    private lateinit var photo: File
    val photoPath = MutableStateFlow<String>("")
    val description = mutableStateOf("")
    val title = mutableStateOf("")
    val cameraPermissionGranted = mutableStateOf(false)

    private val _uiState  = MutableStateFlow<AddPostUiState>(AddPostUiState.Resume)

    val uiState: StateFlow<AddPostUiState>
        get() = _uiState

    // camara
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val imageCapture: ImageCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()
    private val cameraSelector: CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()
    lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }
    fun navigateBack() {
        routeNavigator.navigateToRoute(OptionsRoutes.Home.route)
    }
    fun setCameraProvider(context: Context) {
        cameraProvider = ProcessCameraProvider.getInstance(context)
    }
    fun bindPreview(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
    ) {
        val preview: Preview = Preview.Builder()
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)
        var camera = cameraProvider.get().bindToLifecycle(lifecycleOwner, cameraSelector, imageCapture, preview)
    }
    fun makePhoto(context: Context) {
//        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File("")).build()
        imageCapture.let { imageCapture ->
            // Create time stamped name and MediaStore entry.
            val photoName = SimpleDateFormat("y-mm-dd", Locale.US).format(System.currentTimeMillis())
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, photoName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${context.getString(R.string.app_name)}")
            }
            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    context.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
                .build()

            imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e("APP_TAG", "Photo capture failed: ${exc.message}", exc)
                    }
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        output.savedUri?.let {
                            photoPath.value = getPath(it, context)
                        }
                    }
                }
            )
        }
    }
    fun resumeCamera() {
        File(photoPath.value).delete()
        photoPath.value = ""
    }
    fun stopCamera() {
        cameraProvider.get().unbindAll()
    }
    fun addPost(context: Context) {
        _uiState.value = AddPostUiState.Resume
        if (photoPath.value == "")
            return

        _uiState.value = AddPostUiState.Sending
        val photoFile = File(photoPath.value)
        viewModelScope.launch {
            val response = repository.addPost(
                title = title.value,
                photo = photoFile,
                description = description.value
            )
            _uiState.value = AddPostUiState.Resume
            when(response) {
                is ApiResponse.Success -> {
                    _uiState.value = AddPostUiState.Success(response.data)
                    navigateBack()
                }
                is ApiResponse.ErrorWithMessage -> {
                    _uiState.value = AddPostUiState.ErrorWithMessage(response.messages)
                }
                is ApiResponse.Error -> {
                    _uiState.value = AddPostUiState.Error(response.exception)
                }
            }
            photoFile.delete()
        }
    }

    fun navigateToMapScreen(){
        routeNavigator.navigateToRoute(OptionsRoutes.MapScreen.route)
    }

    private fun getPath(uri: Uri, context: Context): String {
        var path = ""
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, data, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            path = cursor.getString(columnIndex)
            cursor.close()
        }
        return path
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                AddPostViewModel(app.postRepository, dataStoreManager = app.dataStoreManager)
            }
        }
    }
}