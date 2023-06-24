package org.ufind.ui.screen.home.post

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.ufind.R
import org.ufind.data.model.PostModel


//@Preview(showBackground = true)
@Composable
fun ItemPost(post: PostModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
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
            BottomBarPostIcons()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostImage(
    url: String,
    modifier:Modifier = Modifier
) {
    GlideImage(
        model = url,
        contentDescription = "Imagen de post",
        modifier = modifier,
    )
}

@Composable
fun BottomBarPostIcons() {
    val context = LocalContext.current

    Row(Modifier.fillMaxWidth()) {
        Image(
            imageVector = Icons.Filled.Comment,
            contentDescription = "",
            Modifier.padding(16.dp, 0.dp)
        )

        Image(
            imageVector = Icons.Filled.BookmarkBorder,
            contentDescription = "",
            Modifier.padding(16.dp, 0.dp)
        )

        IconButton(
            onClick = { compartirPost(context, "Texto del post que deseas compartir") },
            Modifier.padding(16.dp, 0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Compartir",
                tint = Color.Black
            )
        }
    }
}

private fun compartirPost(context: Context, texto: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, texto)
    intent.setPackage("com.whatsapp") // Especifica que se utilice WhatsApp para compartir

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
    }
}
