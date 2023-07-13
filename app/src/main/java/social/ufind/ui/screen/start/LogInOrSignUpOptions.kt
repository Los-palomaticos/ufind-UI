package social.ufind.ui.screen.start

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ufind.R
import social.ufind.ui.screen.home.post.ImageLogo
@Composable
fun LoginOrSignUpOptions(
    onClickSignUpScreen: () -> Unit = {},
    onClickSignInScreen: () -> Unit = {}
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LogInOrSignUpOptionsBody(
            Modifier
                .widthIn(0.dp,500.dp)
                .align(Alignment.Center),
            onClickSignUpScreen,
            onClickSignInScreen
        )
    }
}


@Composable
fun LogInOrSignUpOptionsBody(
    modifier: Modifier,
    onClickSignUpScreen: () -> Unit ={},
    onClickSignInScreen: () -> Unit ={}
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ImageLogo(size = 175, Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(16.dp))
        GoToSignUpScreen(onClickSignUpScreen)
        Spacer(modifier = Modifier.size(8.dp))
        GoToLogInScreenButton(onClickSignInScreen)
    }

}
@Composable
fun GoToSignUpScreen(onClickSignUpScreen: () -> Unit = {}) {
    Button(
        onClick = onClickSignUpScreen,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text("¡Comenzar a Buscar!")
    }
}

@Composable
fun GoToLogInScreenButton(onClickSignInScreen: () -> Unit) {
    Button(
        onClick = onClickSignInScreen,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text("Iniciar sesión")
    }
}
