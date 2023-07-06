package social.ufind.ui.screen.newchat


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.ufind.R
import social.ufind.data.OptionsRoutes
import social.ufind.firebase.FirebaseViewModel
import social.ufind.firebase.firebaseViewModel
import social.ufind.firebase.model.User
import social.ufind.ui.screen.home.MainChatTopBar


@Composable
fun ContactsScreen(
    viewModel: FirebaseViewModel = firebaseViewModel,
    onClickGotoNewChat: () -> Unit = {},
    navController: NavHostController
) {
    var users = remember {
        mutableStateListOf<User>()
    }
    var username = remember {
        mutableStateOf("")
    }
    firebaseViewModel.getUserByEmail(Firebase.auth.currentUser?.email!!) { user ->
        username.value = user.name
    }

    firebaseViewModel.subscribeToRealtimeUpdates {
        users.clear()
        users.addAll(it)
    }
    val localContext = LocalContext.current.applicationContext

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 4.dp, top = 0.dp, end = 4.dp, bottom = 48.dp)) {
        MainChatTopBar()
        Spacer(modifier = Modifier.size(8.dp))
        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            itemsIndexed(users) { _, item ->
                Row() {
                    UserContact(user = item, onClickGotoNewChat, navController)
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun UserContact(user: User, onClickGotoNewChat: () -> Unit = {}, navController: NavHostController) {
    Column(modifier = Modifier
        .padding(8.dp, 8.dp)

        .fillMaxSize()
        .clickable {

        }) {

        Card(
            onClick = {
                firebaseViewModel.creteChatWith(user)
                val gson: Gson = GsonBuilder().create()
                val userJson = gson.toJson(user)
                navController.navigate(
                    OptionsRoutes.ChatScreen2.route
                        .replace(
                            oldValue = "{user}",
                            newValue = userJson
                        )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Row(
                ) {

                IconButton(onClick = { /*TODO*/ }) {
                    androidx.compose.material3.Icon(
                        Icons.Filled.Person,
                        contentDescription = "",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(10.dp))

                    )
                }
                    Spacer(modifier = Modifier.width(16.dp))

                Column(
                ) {
                    Text(text = user.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = user.email, fontSize = 12.sp, fontStyle = FontStyle.Italic)
                }
            }
            }

        }
    }
    Spacer(modifier = Modifier.size(8.dp))
}


@Preview(showBackground = true)
@Composable
fun PreviewContacts() {
    val navController = rememberNavController()
    UserContact(user = User("marioua289@gmail.com", "mario"), navController = navController)
}