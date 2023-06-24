package org.ufind.ui.screen.userpost.addpost.ui

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import org.ufind.R
import org.ufind.data.OptionsRoutes
import org.ufind.navigation.NavRoute
import org.ufind.ui.screen.home.post.add.AddPostUiState
import org.ufind.ui.screen.home.post.add.viewmodel.AddPostViewModel
import org.ufind.ui.screen.login.LoginUiState


object AddPostScreen: NavRoute<AddPostViewModel> {
    override val route: String
        get() = OptionsRoutes.AddPostScreen.route

    @Composable
    override fun viewModel(): AddPostViewModel = viewModel<AddPostViewModel>(
        factory = AddPostViewModel.Factory
    )
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content(viewModel: AddPostViewModel) {
        AddPostScreen(viewModel = viewModel)
    }

}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddPostScreen(viewModel: AddPostViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.checkPermissions(LocalContext.current)
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderAddPost(Modifier.align(Alignment.TopStart))
        BodyAddPost(
            uiState = uiState.value,
            viewModel = viewModel,
            modifier = Modifier.align(Alignment.Center)
        )
    }

}

@Composable
fun HeaderAddPost(modifier: Modifier) {
    Box(modifier = modifier) {
        Text(text = "", color = colorResource(id = R.color.text_color), fontSize = 16.sp)
    }
}
@Composable
fun HandleUiState(uiState: AddPostUiState) {
    when(uiState){
        is AddPostUiState.ErrorWithMessage -> {
            uiState.errorMessages.forEach { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is AddPostUiState.Success -> {
            Toast.makeText(LocalContext.current, uiState.message, Toast.LENGTH_LONG).show()
        }
        is AddPostUiState.Error -> {
            Text(
                text = "Error desconocido",
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}
@Composable
fun BodyAddPost(
        uiState: AddPostUiState,
        viewModel: AddPostViewModel,
        modifier: Modifier
    ) {
    val photo = viewModel.photoUri.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Column(modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        if (photo.value == Uri.EMPTY)
            CameraPreview(viewModel = viewModel)
        else {
            viewModel.stopCamera()
            Box{
                AsyncImage(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp), model = photo.value, contentDescription = null)
                Button(onClick = {viewModel.resumeCamera()}){
                    Text(text="De nuevo")
                }
            }
        }
        Spacer (Modifier.size(64.dp))
        TitleTextFieldPost(viewModel.title.value) { viewModel.title.value = it }
        Spacer(Modifier.size(16.dp))
        DescriptionTextFieldPost(viewModel.description.value) { viewModel.description.value = it }
        Spacer(Modifier.size(32.dp))
        LocationCardPost()
        Spacer(Modifier.size(32.dp))
        ButtonAddPost(uiState){
            viewModel.addPost(context)
        }
    }
}

@Composable
fun ButtonAddPost(uiState: AddPostUiState, onClickBackToUserInterface: () -> Unit = {}) {
    Button(
        onClick = onClickBackToUserInterface,
        modifier = Modifier.fillMaxWidth(),
        enabled = uiState !is AddPostUiState.Sending,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.text_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text("Publicar")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionTextFieldPost(postDescription: String, onTextChanged: (String) -> Unit) {

    TextField(
        value = postDescription,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Escribe una descripción",
                color = Color.Gray
            )
        },
        maxLines = 3,
        singleLine = false,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = colorResource(id = R.color.text_color),
            unfocusedIndicatorColor = colorResource(id = R.color.text_color)
        )

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTextFieldPost(postTitle: String, onTextChanged: (String) -> Unit) {

    TextField(
        value = postTitle,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Título de la publicación",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = colorResource(id = R.color.text_color),
            unfocusedIndicatorColor = colorResource(id = R.color.text_color)
        )

    )
}

@Composable
fun LocationCardPost() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ), colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Ubicación")
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Universidad Centroamericana José Simeón Cañas")
        }
    }

}



@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    viewModel: AddPostViewModel
) {
    viewModel.setCameraProvider(LocalContext.current)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    Surface {
        Box (modifier = Modifier.fillMaxWidth()) {
            AndroidView(factory = {context ->
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    post {
                        viewModel.cameraProvider.addListener(
                            {
                                viewModel.bindPreview(lifecycleOwner, this)
                            },
                            ContextCompat.getMainExecutor(context)
                        )
                    }
                }},
                modifier = Modifier.fillMaxWidth(),
                update = {
                }

            )
            Row(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)){
                IconButton(onClick = { viewModel.makePhoto(context) }) {
                    Icon(imageVector = Icons.Default.Camera, contentDescription = "Capture", tint = colorResource(
                        id = R.color.white), modifier = Modifier.size(32.dp) )
                    
                }
            }
        }
    }
}