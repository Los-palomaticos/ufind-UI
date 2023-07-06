package social.ufind.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R
import social.ufind.data.model.PostModel

@Preview(showBackground = true)
@Composable
fun MainChatScreen(onClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {
        MainChatScreenBody(onClick)
    }
}

@Composable
fun MainChatScreenBody(onClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column() {
            MainChatTopBar()
            MainChatBody(onClick)
        }
    }
}
@Composable
fun MainChatBody(onClick: () -> Unit = {}) {
    Column() {
        Spacer(modifier = Modifier.size(16.dp))
        PreviewLazyCard(onClick)
    }
}


@Composable
fun MainChatTopBar() {
    Card(

        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.textfield_color)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.Message,
                    contentDescription = "",
                    tint = colorResource(id = R.color.text_color),
                    modifier = Modifier.size(30.dp)
                )
            }

            androidx.compose.material3.Text(
                text = "Chats",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

        }
    }
}

@Composable
fun PostList(onClick: () -> Unit) {
    val myPost: List<PostModel> = emptyList()
    Column(Modifier.verticalScroll(rememberScrollState())) {

        UserCard(message = "Usuario", subMessage = "........", time = "7:30", onClick)
        Spacer(modifier = Modifier.height(8.dp))

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(message: String, subMessage: String, time: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
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


@Composable
fun PreviewLazyCard(onClick: () -> Unit) {
    PostList(onClick)
}