package org.ufind.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ufind.data.model.PostModel
import org.ufind.ui.screen.home.post.ItemPost


@Composable
fun PostList() {
    val myPost: List<PostModel> = emptyList()
    Column(Modifier.verticalScroll(rememberScrollState())) {

        UserCard(message = "Thanos", subMessage = "destruire el mundo", time = "7:30")
        Spacer(modifier = Modifier.height(8.dp))
        UserCard(message = "David", subMessage = "destruire el mundo", time = "7:30")
        Spacer(modifier = Modifier.height(8.dp))
        UserCard(message = "UMana", subMessage = "destruire el mundo", time = "7:30")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainChatScreen(){
        PreviewLazyCard()
    }

@Composable
fun UserCard(message: String, subMessage: String, time: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Usuario",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 16.dp)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = message,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = subMessage,
                        style = TextStyle(color = Color.Gray)
                    )
                }
                Text(
                    text = time,
                    style = TextStyle(color = Color.Gray),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserCard(){
    UserCard(message = "Hola, este es un mensaje de ejemplo", subMessage = "Mensaje enviado hace 5 minutos", time = "09:30 AM")


}

@Preview
@Composable
fun PreviewLazyCard(){
    PostList()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(){
    val messages = remember { mutableStateListOf<String>() }
    val newMessage = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messages) { message ->
                UserCard(message = message, subMessage = message, time = message)
            }
        }

        TextField(
            value = newMessage.value,
            onValueChange = { newMessage.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                messages.add(newMessage.value)
                newMessage.value = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Enviar")
        }
    }
}