package social.ufind.ui.screen.home.post

import android.content.Context
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ufind.R
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.ui.screen.home.post.viewmodel.PostViewModel


//@Preview(showBackground = true)
@Composable
fun ItemPost(post: PostWithAuthorAndPhotos?, viewModel: PostViewModel) {
    val isOptionsExpanded = remember{ mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
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
                    "Por ${post!!.publisher.username}",
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
                text = post!!.post.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start),
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = post.post.description,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(16.dp))
            PostImage(url = post.photos.first().photo, modifier = Modifier.fillMaxWidth())
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

    }
}

@Composable
fun BottomBarPostIcons(
    context: Context, post: PostWithAuthorAndPhotos,
    viewModel: PostViewModel
) {
    val isSaved = remember { mutableStateOf(false) } //para guardar icono

    val scope = rememberCoroutineScope() // guarda el estado de la corrutina

    val descargarYCompartirImagen: suspend () -> Unit = {
        val imagenDescargada = withContext(Dispatchers.IO) {
            viewModel.descargarImagen(context, post.photos.first().photo)
        }
        viewModel.compartirContenido(context, "Mira Este Objeto Perdido", imagenDescargada)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        Image(
            imageVector = if (isSaved.value) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = "",
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)
                .clickable {
                    isSaved.value = !isSaved.value
                }
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            imageVector = Icons.Filled.Mail,
            contentDescription = "",
            Modifier
                .padding(end = 16.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)
                .clickable {
                    viewModel.enviarCorreo(context, post.publisher.email, "Post de Ufind")
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
                        descargarYCompartirImagen()
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