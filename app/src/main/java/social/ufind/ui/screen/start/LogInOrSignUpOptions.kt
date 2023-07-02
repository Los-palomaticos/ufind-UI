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
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Simula un tiempo de carga
        delay(2000)
        isLoading = false
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            LogInOrSignUpOptionsBody(
                Modifier.align(Alignment.Center),
                onClickSignUpScreen,
                onClickSignInScreen
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    val transition = rememberInfiniteTransition()
    val xOffset by transition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Icono de lupa",
                modifier = Modifier
                    .size(64.dp)
                    .offset(x = xOffset.dp)
            )
        }
    }
}

@Composable
fun LogInOrSignUpOptionsBody(
    modifier: Modifier,
    onClickSignUpScreen: () -> Unit ={},
    onClickSignInScreen: () -> Unit ={}
) {
    Column(modifier = modifier) {
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
