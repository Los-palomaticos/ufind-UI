package social.ufind.ui.screen.changepassword

import android.util.Patterns
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
import social.ufind.ui.screen.home.post.ImageLogo


@Preview(showBackground = true)
@Composable
fun ForgottenPasswordScreen(onClickEnterCodeForgottenPasswordScreen: () -> Unit={}) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderForgottenPassword(Modifier.align(Alignment.TopCenter))
        BodyForgottenPassword(Modifier.align(Alignment.Center), onClickEnterCodeForgottenPasswordScreen)

    }

}

@Composable
fun BodyForgottenPassword(modifier: Modifier, onClickEnterCodeForgottenPasswordScreen: () -> Unit = {}) {
    var emailToChangePassword by rememberSaveable {
        mutableStateOf("")
    }
    var isSendCodeAvailable by rememberSaveable {
        mutableStateOf(false)

    }

    Column(modifier = modifier) {

        Text(
            "Introduzca su email", fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = colorResource(
                id = R.color.text_color
            )
        )
        Spacer(modifier = Modifier.size(16.dp))
        EmailToChangePassword(emailToChangePassword) {
            emailToChangePassword = it
            isSendCodeAvailable = enableSendCode(emailToChangePassword)

        }
        Spacer(modifier = Modifier.size(16.dp))
        SendCodeButton(isSendCodeAvailable, emailToChangePassword, onClickEnterCodeForgottenPasswordScreen)


    }

}

@Composable
fun SendCodeButton(
    sendCodeAvailable: Boolean,
    emailToChangePassword: String,
    onClickEnterCodeForgottenPasswordScreen: () -> Unit ={}
) {
    Button(
        onClick = onClickEnterCodeForgottenPasswordScreen,
        enabled = sendCodeAvailable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White

        )
    ) {
        Text("Enviar código")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailToChangePassword(
    emailToChangePassword: String,
    onTextChanged: (String) -> Unit,
) {
    TextField(
        value = emailToChangePassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
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

@Composable
fun HeaderForgottenPassword(modifier: Modifier) {
    Box(modifier = Modifier.fillMaxWidth()) {
        ImageLogo(size = 150, modifier = modifier)
    }
}

fun enableSendCode(email: String): Boolean {

    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

