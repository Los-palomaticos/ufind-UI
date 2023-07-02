package social.ufind.ui.screen.home.post

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ufind.R
import social.ufind.data.model.PostModel
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


//@Preview(showBackground = true)
@Composable
fun ItemPost(post: PostModel) {
    val isOptionsExpanded = remember { mutableStateOf(false) }
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
                    "Por ${post.publisher.username}",
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
                            isOptionsExpanded.value = true
                        }
                )
            }
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
        contentDescription = "Imagen de post",
        modifier = modifier,
    )
}
@Composable
fun BottomBarPostIcons(context: Context, post: PostModel) {
    val isSaved = remember { mutableStateOf(false) } // para guardar el estado del icono

    val scope = rememberCoroutineScope() // guarda el estado de la corrutina

    val descargarYCompartirImagen: suspend () -> Unit = {
        val imagenDescargada = withContext(Dispatchers.IO) {
            descargarImagen(context, post.photos.first())
        }
        compartirContenido(context, "Mira Este Objeto Perdido", imagenDescargada)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        Image(
            imageVector = if (isSaved.value) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = "",
            Modifier
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
                    enviarCorreo(context, "${post.publisher.email}", "Post de Ufind")
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
        Toast.makeText(
            context,
            "No se encontraron aplicaciones de compartir disponibles",
            Toast.LENGTH_SHORT
        ).show()
    }
}


private fun enviarCorreo(context: Context, destinatario: String, asunto: String) {
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
