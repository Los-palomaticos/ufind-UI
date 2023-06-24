package org.ufind.ui.screen.home.post

import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.ufind.R
import org.ufind.data.model.PostModel

@Composable
fun PageHeader() {
    ImageLogo(80, modifier = Modifier)
    PageHeaderLineDivider()
}

@Composable
fun PageHeaderLineDivider() {
    Divider(
        Modifier
            .height(1.dp)
            .fillMaxWidth(),
        color = colorResource(id = R.color.grey01)
    )
}

@Composable
fun ImageLogo(size: Int, modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_ufind),
        contentDescription = "Logo",
        modifier = modifier.size(size.dp),
        contentScale = ContentScale.Fit
    )
}

@Preview(showBackground = true)
@Composable
fun PostScreen(onClickAddPostScreen: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageHeader()
        Text(text = "Publicaciones", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Start).padding(0.dp, 16.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            PostList()
            AddPostFloatingButton(onClickAddPostScreen, Modifier.align(Alignment.BottomEnd))


        }
    }
}

@Composable
fun PostList() {
    val myPost: List<PostModel> = emptyList()
    Column(Modifier.verticalScroll(rememberScrollState())) {
        ItemPost()
        ItemPost()
        ItemPost()
        ItemPost()
        ItemPost()
        ItemPost()
    }
}

@Composable
fun PostImage(size: Int) {
    Image(
        imageVector = Icons.Filled.AddAPhoto,
        contentDescription = "",
        modifier = Modifier.size(size.dp),
        alignment = Alignment.Center
    )

}

@Composable
fun BottomBarPostIcons() {
    Row(
        Modifier
            .fillMaxWidth()
    ) {
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
        Image(
            imageVector = Icons.Filled.Share, contentDescription = "", Modifier.padding(16.dp, 0.dp)
        )


    }
}
@Composable
fun AddPostFloatingButton(
    onClickAddPostScreen: () -> Unit = {},
    modifier: Modifier
) {
    val context = LocalContext.current
    var cameraPermissionGranted by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        cameraPermissionGranted = isGranted
        if (isGranted) {
            // El permiso de la cámara se otorgó correctamente
            Toast.makeText(context, "Permiso de cámara otorgado", Toast.LENGTH_SHORT).show()
        } else {
            // El permiso de la cámara se denegó
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    FloatingActionButton(
        onClick = {
            if (!cameraPermissionGranted) {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            } else {
                onClickAddPostScreen()
            }
        },
        modifier = modifier.padding(0.dp, 42.dp),
        backgroundColor = colorResource(id = R.color.text_color)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "", tint = Color.White)
    }
}