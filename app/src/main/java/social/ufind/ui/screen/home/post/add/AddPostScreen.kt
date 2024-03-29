package social.ufind.ui.screen.home.post.add

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import org.ufind.R
import social.ufind.navigation.OptionsRoutes
import social.ufind.navigation.NavRoute
import social.ufind.ui.screen.home.post.add.viewmodel.AddPostViewModel
import social.ufind.ui.screen.home.user.settings.HeaderConfigurationCard

object AddPostScreen : NavRoute<AddPostViewModel> {
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

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderAddPost(Modifier.align(Alignment.TopStart))
        BodyAddPost(
            uiState = uiState.value,
            viewModel = viewModel,
            modifier = Modifier.align(Alignment.Center).widthIn(0.dp,500.dp)
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
    when (uiState) {
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
    val photo = viewModel.photoPath.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderConfigurationCard(
            title = "Crear publicación",
            onClick = { viewModel.navigateBack() }
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HandleUiState(uiState = uiState)
            Surface {
                Box(modifier = Modifier.fillMaxWidth()){
                    if (photo.value != "")
                        viewModel.stopCamera()

                    CameraPreview(
                        viewModel = viewModel,
                        photo = photo
                    )

                }
            }
            Spacer(Modifier.size(64.dp))
            TitleTextFieldPost(viewModel.title.value) { viewModel.title.value = it }
            Spacer(Modifier.size(16.dp))
            DescriptionTextFieldPost(viewModel.description.value) {
                viewModel.description.value = it
            }
            Spacer(Modifier.size(32.dp))
//            LocationCardPost {
//                viewModel.navigateToMapScreen()
//            }
            Spacer(Modifier.size(32.dp))
            ButtonAddPost(uiState) {
                viewModel.addPost(context)
            }

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

@Preview()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationCardPost(onClickGoToMapScreen: () -> Unit = {}) {
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
            Text(
                text = "Ubicación",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.mapbutton),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Universidad Centroamericana José Simeón Cañas",
                            textAlign = TextAlign.Center,
                            color = colorResource(id = R.color.text_color),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            /*            Card(
                            onClick = onClickGoToMapScreen,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {

                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.mapbutton),
                                    contentDescription = "Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Ir a mapa",
                                        textAlign = TextAlign.Center,
                                        color = colorResource(id = R.color.text_color),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }*/
        }
    }
}


@Composable
fun CameraPreview(
    viewModel: AddPostViewModel,
    photo: State<String>
) {
    viewModel.setCameraProvider(LocalContext.current)
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    Box(
        modifier = Modifier.height(500.dp)
    ) {
        if (photo.value == "") {
            AndroidView(factory = {
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
                update = {
                    Log.d("APP_TAG", "UPDATE")
                    // bind camera again if photo is empty
                    if (photo.value == "") {
                        viewModel.bindCamera(previewView = it, lifecycleOwner = lifecycleOwner, context = context)
                    }
                }, modifier = Modifier.fillMaxWidth())
        }
        else {
            viewModel.stopCamera()
            AsyncImage(
                model = photo.value,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = null
            )
        }
        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 20.dp)){
            if (photo.value == "") {
                IconButton(onClick = { viewModel.makePhoto(context) }) {
                    Icon(imageVector = Icons.Default.Camera, contentDescription = "Capture", tint = colorResource(
                        id = R.color.white), modifier = Modifier.size(40.dp) )
                }
            } else {
                IconButton(onClick = { viewModel.resumeCamera() }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Capturar de nuevo", tint = colorResource(
                        id = R.color.white), modifier = Modifier.size(40.dp))

                }
            }
        }
    }
}