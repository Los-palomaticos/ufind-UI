package org.ufind.ui.screen.home.post.add.viewmodel

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableStateFlow
import org.ufind.R
import org.ufind.UfindApplication
import org.ufind.data.datastore.DataStoreManager
import org.ufind.navigation.RouteNavigator
import org.ufind.navigation.UfindNavigator
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

class AddPostViewModel(
    val repository: String,
    val dataStoreManager: DataStoreManager,
    val routeNavigator: UfindNavigator = UfindNavigator()
    ): ViewModel(), RouteNavigator by routeNavigator, DefaultLifecycleObserver {
    lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val imageCapture: ImageCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    private val cameraSelector: CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    val postPhoto = MutableStateFlow<String>("")

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
                        val savedUri = output.savedUri
                        postPhoto.value = savedUri.toString()
                        Log.d("APP_TAG", "Photo capture succeeded: $savedUri")
                    }
                }
            )
        }
    }


    fun resumeCamera() {
        postPhoto.value = ""
    }

//    override fun onResume(owner: LifecycleOwner) {
//        super.onResume(owner)
//        cameraProvider.get().bindToLifecycle(owner, )
//    }
    fun stopCamera() {
        cameraProvider.get().unbindAll()
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                AddPostViewModel(repository = "", dataStoreManager = app.dataStoreManager)
            }
        }
    }
}