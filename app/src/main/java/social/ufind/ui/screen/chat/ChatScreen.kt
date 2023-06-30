package social.ufind.ui.screen.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults


@Preview(showBackground = true)
@Composable
fun ChatScreen() {
    // Estado para almacenar el texto del TextField
    val textFieldValue = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /* Acción al hacer clic en el botón Ver Publicación */ },
                modifier = Modifier
                    .padding(end = 20.dp),
                shape = RoundedCornerShape(18.dp),


            ) {
                Text(text = "Ver Publicación")
            }

            Button(
                onClick = { /* Acción al hacer clic en el botón Completar */ },
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(text = "Completar")
            }
        }


    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textFieldValue.value,
                onValueChange = { textFieldValue.value = it },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { /* Acción al hacer clic en el botón de enviar */ },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Enviar", )
            }
        }
    }
}
