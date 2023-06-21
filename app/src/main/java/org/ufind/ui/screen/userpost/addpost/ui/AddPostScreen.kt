package org.ufind.ui.screen.userpost.addpost.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R

@Preview(showBackground = true)
@Composable
fun AddPostScreen(onClickBackToUserInterface: () -> Unit = {}) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderAddPost(Modifier.align(Alignment.TopStart))
        BodyAddPost(Modifier.align(Alignment.Center), onClickBackToUserInterface)
    }

}

@Composable
fun HeaderAddPost(modifier: Modifier) {
    Box(modifier = modifier) {
        Text(text = "", color = colorResource(id = R.color.text_color), fontSize = 16.sp)
    }
}

@Composable
fun BodyAddPost(modifier: Modifier, onClickBackToUserInterface: () -> Unit = {}) {
    var postDescription by remember {
        mutableStateOf("")
    }
    var postTitle by remember {
        mutableStateOf("")
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        AddPostImage(150)
        Spacer (Modifier.size(64.dp))
        TitleTextFieldPost(postTitle) { postTitle = it }
        Spacer(Modifier.size(16.dp))
        DescriptionTextFieldPost(postDescription) { postDescription = it }
        Spacer(Modifier.size(32.dp))
        LocationCardPost()
        Spacer(Modifier.size(32.dp))
        ButtonAddPost(onClickBackToUserInterface)



    }
}

@Composable
fun AddPostImage(size:Int) {
    Image(imageVector = Icons.Filled.AddAPhoto, contentDescription = "", modifier = Modifier.size(size.dp), alignment = Alignment.Center)
}

@Composable
fun ButtonAddPost(onClickBackToUserInterface: () -> Unit = {}) {
    Button(
        onClick = onClickBackToUserInterface,
        modifier = Modifier.fillMaxWidth(),
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
