package org.ufind.ui.screen.home.post

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ufind.R
import org.ufind.data.model.PostModel
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


//@Preview(showBackground = true)
@Composable
fun ItemPost(post: PostModel) {
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
            Text(
                "Por ${post.publisher.username}",
                color = colorResource(id = R.color.disabled_color),
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = post.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start),
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = post.description,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(16.dp))
            PostImage(url = post.photos.first(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.size(16.dp))
            BottomBarPostIcons(context = LocalContext.current, post = post)
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
        contentDescription = "Imagen de post",
        modifier = modifier,
    )
}

@Composable
fun BottomBarPostIcons(context: Context, post: PostModel) {
    val isSaved = remember { mutableStateOf(false) } //para guardar icono

    val scope = rememberCoroutineScope() //guarda estado de corrutina

    val descargarYCompartirImagen: suspend () -> Unit = {
        val imagenDescargada = withContext(Dispatchers.IO) {
            descargarImagen(context, post.photos.first())
        }
        compartirContenido(context, "Mira Este Objeto Perdido", imagenDescargada)
    }

    Row(Modifier.fillMaxWidth()) {
        Image(
            imageVector = Icons.Filled.Comment,
            contentDescription = "",
            Modifier.padding(16.dp, 0.dp)
                .clickable {
                    enviarCorreoGmail(context, "correo@example.com", "Asunto del correo")
                }
        )

        Image(
            imageVector = if (isSaved.value) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = "",
            Modifier.padding(16.dp, 0.dp)
                .clickable {
                    isSaved.value = !isSaved.value
                }
        )

        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = "Compartir",
            tint = Color.Black,
            modifier = Modifier.padding(16.dp, 0.dp)
                .clickable {
                    scope.launch {
                        descargarYCompartirImagen()
                    }
                }
        )
    }
}

private suspend fun descargarImagen(context: Context, url: String): File {
    return withContext(Dispatchers.IO) {
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

        file
    }
}


private fun compartirContenido(context: Context, texto: String, file: File) {
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
        Toast.makeText(context, "No se encontraron aplicaciones de compartir disponibles", Toast.LENGTH_SHORT).show()
    }
}


private fun enviarCorreoGmail(context: Context, destinatario: String, asunto: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(destinatario))
    intent.putExtra(Intent.EXTRA_SUBJECT, asunto)
    intent.setPackage("com.google.android.gm") // Establecer el paquete de Gmail

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No se encontró la aplicación de Gmail", Toast.LENGTH_SHORT).show()
    }
}