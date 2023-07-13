package social.ufind.ui.screen.changepassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R

@Preview(showBackground = true)
@Composable
fun EnterCodeForgottenPasswordScreen(onClickChangePasswordScreen: () -> Unit={}) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderForgottenPassword(Modifier.align(Alignment.TopCenter))
        EnterCodeBody(Modifier.align(Alignment.Center), onClickChangePasswordScreen)

    }

}

@Composable
fun EnterCodeBody(modifier: Modifier, onClickChangePasswordScreen: () -> Unit={}) {
    var sentCodePsw by rememberSaveable {
        mutableStateOf("")
    }
    var isRequestAvailablePsw by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = modifier) {
        Text(
            "Ingresar código enviado a su email", fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = colorResource(
                id = R.color.text_color
            )
        )
        Spacer(modifier = Modifier.size(16.dp))
        CodeToChangePassword(sentCodePsw) {
            sentCodePsw = it
            isRequestAvailablePsw = acceptRequesToChangePsw(sentCodePsw)
        }
        Spacer(modifier = Modifier.size(16.dp))
        SendRequestToChangePswButton(isRequestAvailablePsw, sentCodePsw,onClickChangePasswordScreen)

    }

}

@Composable
fun SendRequestToChangePswButton(
    requestAvailablePsw: Boolean,
    sentCodePsw: String,
    onClickChangePasswordScreen: () -> Unit
) {
    Button(
        onClick = onClickChangePasswordScreen,
        enabled = requestAvailablePsw,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White

        )
    ) {
        Text("Enviar")
    }
}

fun acceptRequesToChangePsw(code: String): Boolean {
    return true
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeToChangePassword(sentCodePsw: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = sentCodePsw,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Código de verificación") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorResource(id = R.color.textfield_color),
            focusedIndicatorColor = colorResource(id = R.color.primary_color),
            unfocusedIndicatorColor = Color.Transparent
        )

    )

}
