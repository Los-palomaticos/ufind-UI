package social.ufind.ui.screen.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.ufind.R

@Composable
fun ConnectionErrorScreen(onClickLoadingScreen: () -> Unit = {}) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(0.dp, 500.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ha ocurrido un error de red, intenta conectarte nuevamente")
            Button(
                onClick = onClickLoadingScreen,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary_color),
                    disabledContainerColor = colorResource(id = R.color.disabled_color),
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                )
            ) {
                Text("Reintentar")
            }
        }
    }
}