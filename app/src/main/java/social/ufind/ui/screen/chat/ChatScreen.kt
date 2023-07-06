package social.ufind.ui.screen.chat


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ufind.R
import social.ufind.ui.screen.chat.chatviewmodel.ChatViewModel
import social.ufind.ui.screen.home.post.PageHeaderLineDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel =  viewModel(),
    onClickGoToMainChatScreen: () -> Unit = {}
) {
    val message: String by chatViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by chatViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        TopAppBarChat(onClickGoToMainChatScreen)
        Spacer(modifier = Modifier.size(4.dp))
        PageHeaderLineDivider()
        Spacer(modifier = Modifier.size(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean

                SingleMessage(
                    message = message[Constants.MESSAGE].toString(),
                    isCurrentUser = isCurrentUser,
                    time = Constants.SENT_ON
                )
            }
        }

        androidx.compose.material3.TextField(
            value = message,
            onValueChange = { chatViewModel.updateMessage(it) },
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
                        chatViewModel.addMessage()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Button"
                    )
                }
            }
        )

    }
}

@Composable
fun TopAppBarChat(onClickGoToMainChatScreen: ()->Unit = {}){
    androidx.compose.material3.Card(

        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick =onClickGoToMainChatScreen) {
                androidx.compose.material3.Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = colorResource(id = R.color.text_color),
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                androidx.compose.material3.Icon(
                    Icons.Filled.Person,
                    contentDescription = "",
                    tint = colorResource(id = R.color.text_color),
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            androidx.compose.material3.Text(
                text = "Marioa",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

        }
    }
}
