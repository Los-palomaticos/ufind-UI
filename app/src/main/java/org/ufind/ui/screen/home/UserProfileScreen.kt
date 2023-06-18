package org.ufind.ui.screen.home


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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.elevatedButtonElevation
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight

@Preview(showBackground = true)
@Composable
fun UserProfileScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProfileBody()







    }
}

@Composable
fun ProfileBody() {
    Column {
        UserInfo()
        SettingsBody()
        EditProfileButton()
        IconProfile()

    }


}

@Composable
fun SettingsBody() {
    Row {

    }

}


@Composable
fun UserInfo() {
    Box {
        ImageWithTexts()


    }

}

@Composable
fun ImageWithTexts() {
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
            Text(text = "Nombre", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "00000000@example.com")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Institución")
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
            .padding(start = 0.dp, end = 0.dp, bottom = 0.dp,)

    ) {
        Text(
            "Editar perfil",
            color = colorResource(id = R.color.white),
            modifier = Modifier.padding(5.dp)
        )



    }

}

@Composable
fun IconProfile(){
    Row {
        Image(painter = painterResource(id = R.drawable.ic_settings), contentDescription = "settings")
        Image(painter = painterResource(id = R.drawable.ic_wallet), contentDescription = "Wallet")
    }

}


