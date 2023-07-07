package social.ufind.ui.screen.home.post.itempost

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ufind.R
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.ui.screen.home.post.viewmodel.PostViewModel

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
fun ItemPost(modifier: Modifier = Modifier, post: PostWithAuthorAndPhotos?, viewModel: ItemPostViewModelMethods) {
    val isOptionsExpanded = remember{ mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .advancedShadow(shadowBlurRadius = 4.dp, alpha = 0.2f, cornersRadius = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
//        elevation = CardDefaults.elevatedCardElevation(
//            focusedElevation = 4.dp
//        )
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
            PostImage(url = post?.photos?.first()?.photo?:"", modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.size(16.dp))
            BottomBarPostIcons(context = LocalContext.current, post = post, viewModel= viewModel)

            if (isOptionsExpanded.value) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Opciones",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Button(
                            onClick = {
                                // Acción al hacer clic en "Borrar publicación"
                                isOptionsExpanded.value = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Text(text = "Borrar publicación")
                        }
                        Button(
                            onClick = {
                                // Acción al hacer clic en "Reportar publicación"
                                isOptionsExpanded.value = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Reportar publicación")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostImage(
    url: String,
    modifier: Modifier = Modifier
) {
    GlideImage(
        model = url,
        modifier = modifier,
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
}

@Composable
fun BottomBarPostIcons(
    context: Context,
    post: PostWithAuthorAndPhotos?,
    viewModel: ItemPostViewModelMethods
) {
    val isSaved = rememberSaveable{ mutableStateOf(post?.post?.isSaved) } //para guardar icono
    val scope = rememberCoroutineScope() // guarda el estado de la corrutina

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        Image(
            imageVector = if (isSaved.value == true) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = "",
            modifier = Modifier
                .padding(end = 16.dp)
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
        Image(
            imageVector = Icons.Filled.Mail,
            contentDescription = "",
            Modifier
                .padding(end = 16.dp)
                .weight(2f)
                .align(Alignment.CenterVertically)
                .clickable {
                    if (post != null) {
                        viewModel.enviarCorreo(context, post.publisher.email, "Post de Ufind")
                    }
                }
        )

        Image(
            imageVector = Icons.Filled.Share,
            contentDescription = "Compartir",
            Modifier
                .padding(end = 16.dp)
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

        Icon(
            imageVector = Icons.Filled.Send,
            contentDescription = "Chat",
            tint = Color.Black,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)
                .clickable {
                    // Acción para el chat
                }
        )
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