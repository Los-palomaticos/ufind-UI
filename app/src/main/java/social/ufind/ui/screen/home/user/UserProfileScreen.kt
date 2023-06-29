package social.ufind.ui.screen.home.user

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ufind.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.elevatedButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import social.ufind.data.OptionsRoutes
import social.ufind.data.datastore.DataStoreManager
import social.ufind.data.model.UserModel
import social.ufind.navigation.NavRoute
import social.ufind.ui.screen.settings.ProfileGoToButtons
import social.ufind.ui.screen.settings.SettingsScreen
import social.ufind.ui.screen.settings.viewmodel.SettingsViewModel


object SettingsScreen: NavRoute<SettingsViewModel> {
    override val route: String
        get() = OptionsRoutes.SettingsScreen.route
    @Composable
    override fun viewModel(): SettingsViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(factory = SettingsViewModel.Factory)
    @Composable
    override fun Content(viewModel: SettingsViewModel) {
        SettingsScreen(viewModel)
    }

}
@Preview(showBackground = true)
@Composable
fun UserProfileScreen(
    onClickProfileSettings: () -> Unit = {},
    onClickWalletButton: () -> Unit = {}
) {
    val data = DataStoreManager(LocalContext.current)

    val user by data.getUserData().collectAsState(initial = UserModel(0, "", "", "", ""))

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProfileBody(user, onClickProfileSettings, onClickWalletButton)
    }
}

@Composable
fun ProfileBody(
    user: UserModel,
    onClickProfileSettings: () -> Unit = {},
    onClickWalletButton: () -> Unit = {}
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        UserInfo(user)
        EditProfileButton()
        Spacer(Modifier.size(16.dp))
        ProfileGoToButtons(onClickProfileSettings, onClickWalletButton)

    }
}

@Composable
fun UserInfo(user: UserModel) {
    Box {
        ImageWithTexts(user)


    }

}

@Composable
fun ImageWithTexts(user: UserModel) {
    Row(modifier = Modifier.padding(16.dp)) {
        // Image
        Image(
            painter = painterResource(id = R.drawable.justinbieber), // Replace with your image resource
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)

        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text Column
        Column(modifier = Modifier.weight(1f)) {
            Text(text = user.username, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Edad")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Institución")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Residencia")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = user.email)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "10000 UPoints")

        }
    }
}

@Composable
fun EditProfileButton() {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.text_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), elevation = elevatedButtonElevation(
            defaultElevation = 40.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, bottom = 0.dp)

    ) {
        Text(
            "Editar perfil",
            color = colorResource(id = R.color.white),
            modifier = Modifier.padding(5.dp)
        )
    }
}

