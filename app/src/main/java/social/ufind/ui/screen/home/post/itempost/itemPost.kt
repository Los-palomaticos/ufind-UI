package social.ufind.ui.screen.home.post.itempost

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ufind.R
import social.ufind.UfindApplication
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.firebase.firebaseViewModel
import social.ufind.firebase.model.User

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}
@Composable
fun ItemPost(
    modifier: Modifier = Modifier,
    post: PostWithAuthorAndPhotos?,
    viewModel: ItemPostViewModelMethods
) {
    val isOptionsExpanded = remember{ mutableStateOf(false) }

    Card(
        modifier = modifier
            .widthIn(0.dp, 500.dp)
            .padding(vertical = 8.dp)
            .advancedShadow(shadowBlurRadius = 4.dp, alpha = 0.2f, cornersRadius = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Por ${post?.publisher?.username}",
                    color = colorResource(id = R.color.disabled_color),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            isOptionsExpanded.value = !isOptionsExpanded.value
                        }
                )
            }
            Spacer(Modifier.size(16.dp))
            Text(
                text = post?.post?.title?:"",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start),
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = post?.post?.description?:"",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(16.dp))
            PostImage(url = post?.photos?.first()?.photo?:"")
            Spacer(Modifier.size(16.dp))
            BottomBarPostIcons(context = LocalContext.current, post = post, viewModel= viewModel)

            if (isOptionsExpanded.value) {
                if (post != null) {
                    PostOptions(
                        viewModel = viewModel,
                        isOptionsExpanded = isOptionsExpanded,
                        id=post.post.id,
                        publisherId = post.post.user_id
                    )
                }
            }

        }
    }
}
@Composable
fun PostOptions (viewModel: ItemPostViewModelMethods, isOptionsExpanded: MutableState<Boolean>, id: Int, publisherId: Int) {
    AlertDialog(
        onDismissRequest = { isOptionsExpanded.value = false },
        title = { Text(text = "Opciones") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                if (viewModel.isOwner(publisherId)) {
                    Button(
                        onClick = {
                            Log.d("APP_TAG", id.toString())
                            viewModel.deletePost(id)
                            isOptionsExpanded.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text_color))
                    ) {
                        Text(text = "Borrar publicación")
                    }
                }
                Button(
                    onClick = {
                        // Acción al hacer clic en el botón "Reportar publicación"
                        viewModel.reportPost(id)
                        isOptionsExpanded.value = false
                        // Agrega aquí el código que deseas ejecutar al hacer clic en el botón
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text_color))
                ) {
                    // Contenido del botón (por ejemplo, texto)
                    Text(text = "Reportar publicación")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isOptionsExpanded.value = false
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text_color))
            ) {
                Text(text = "Aceptar")
            }
        },
        dismissButton = null
    )
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostImage(
    url: String,
    modifier: Modifier = Modifier
) {
    val isPreviewEnable = remember{ mutableStateOf(false) }

    GlideImage(
        model = url,
        modifier = modifier
            .clickable {
                isPreviewEnable.value = true
            },
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
        contentDescription = "Imagen de post"
    ) {
        it.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.no_image)
            .thumbnail(
                it.clone()
                    .load(R.drawable.default_image_loading)
                    .transition(withCrossFade())
            )
    }
    if (isPreviewEnable.value) {
        Dialog(
            onDismissRequest = { isPreviewEnable.value = false },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false)
            )
        {
            Box {
                GlideImage(
                    model = url,
                    modifier = modifier
                        .clickable {
                            isPreviewEnable.value = true
                        },
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    contentDescription = "Imagen de post"
                )
            }
        }
    }
}
@Composable
fun BottomBarPostIcons(
    context: Context,
    post: PostWithAuthorAndPhotos?,
    viewModel: ItemPostViewModelMethods
) {
    val isSaved = rememberSaveable{ mutableStateOf(post?.post?.isSaved) } //para guardar icono
    val scope = rememberCoroutineScope() // guarda el estado de la corrutina
    val userId: Int = UfindApplication.getUserId()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = if (isSaved.value == true) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = "",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .clickable {
                    isSaved.value = !isSaved.value!!
                    if (isSaved.value!!) {
                        if (post != null) {
                            viewModel.savePost(post.post.id)
                        }
                    } else {
                        if (post != null) {
                            viewModel.deleteSavedPost(post.post.id)
                        }
                    }
                }
        )
        if (post?.post?.user_id != userId) {
            Image(
                imageVector = Icons.Filled.Mail,
                contentDescription = "",
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        if (post != null) {
                            viewModel.enviarCorreo(context, post.publisher.email, "Post de Ufind")
                        }
                    }
            )
        }

        Image(
            imageVector = Icons.Filled.Share,
            contentDescription = "Compartir",
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .clickable {
                    scope.launch {
                        descargarYCompartirImagen(
                            post = post,
                            context = context,
                            viewModel = viewModel
                        )
                    }
                }
        )
        if (post?.post?.user_id != userId) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Chat",
                tint = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        if (post != null) {
                            val user = User(post.publisher.email, post.publisher.username)
                            firebaseViewModel.creteChatWith(user)
                            val gson: Gson = GsonBuilder().create()
                            val userJson = gson.toJson(user)
                            viewModel.navigateToChat(userJson)
                        }
                    }
            )
        }
        }
}

suspend fun descargarYCompartirImagen(post: PostWithAuthorAndPhotos?, context: Context, viewModel: ItemPostViewModelMethods) {
    val imagenDescargada = withContext(Dispatchers.IO) {
        post?.photos?.first()?.let { viewModel.descargarImagen(context, it.photo) }
    }
    if (imagenDescargada != null) {
        if (post != null) {
            viewModel.compartirContenido(context, "Mira este objeto perdido:  *${post.post.title}*", imagenDescargada)
        }
    }
}