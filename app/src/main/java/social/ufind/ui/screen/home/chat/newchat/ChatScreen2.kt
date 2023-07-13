package social.ufind.ui.screen.home.chat.newchat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R
import social.ufind.firebase.model.Message
import social.ufind.firebase.model.User
import social.ufind.firebase.firebaseViewModel

@Composable
fun ChatScreen2(otherUser: User, onClickBackToMainChat: () -> Unit = {}) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 16.dp)){
        Column{
            Scaffold(
                modifier = Modifier.padding(8.dp),
                topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    title = {
                        Text(
                            text = otherUser.name, fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onClickBackToMainChat) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                            )
                        }
                    })
            },
                bottomBar = { messageBox(otherUser) }
            ) {
                messageList(user = otherUser, padding = it)
            }

        }
    }
}

@Composable

fun sentMessage(message: Message) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(100.dp, end = 10.dp)
            .clip(RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp))
            .background(color = colorResource(id = R.color.send_text))
            .clickable { }
    ) {
        Row(Modifier.padding(10.dp)) {
            Column(modifier = Modifier.weight(3.0f, true)) {
                Text(
                    text = message.message,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun receivedMessage(message: Message) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(10.dp, end = 100.dp)
            .clip(RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp))
            .background(color = colorResource(id = R.color.chat_remitent))
            .clickable { }

    ) {
        Row(Modifier.padding(10.dp)) {
            Column(modifier = Modifier.weight(3.0f, true)) {
                Text(
                    text = message.message,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp),
                    color = Color.White

                )
            }
        }
    }
}

@Composable
fun messageList(user: User, padding: PaddingValues) {
    var listMessage = remember {
        mutableStateListOf<Message>()
    }
    firebaseViewModel.subscribeToRealtimeMessages(user) {
        listMessage.clear()
        listMessage.addAll(it)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        items(listMessage) { message ->
            Column() {
                Spacer(modifier = Modifier.height(6.dp))
                if (message.email != user.email) {
                    sentMessage(message = message)
                }
                else{
                    receivedMessage(message = message)
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun messageBox(user: User) {
    val textState = remember {
        mutableStateOf(TextFieldValue(""))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { androidx.compose.material3.Text(text = "Escribe un mensaje") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = colorResource(id = R.color.text_color),
                unfocusedIndicatorColor = colorResource(id = R.color.text_color)
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        firebaseViewModel.saveMessage(textState.value.text, user)
                        textState.value = TextFieldValue("")                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Button"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}