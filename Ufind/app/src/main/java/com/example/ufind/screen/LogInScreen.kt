package com.example.ufind.screen

import android.util.Patterns
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ufind.R

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    onClickSignUpScreen: () -> Unit = {},
    onClickUserInterfaceNavigation: () -> Unit = {},
    onClickForgottenPasswordScreen: () -> Unit = {}
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Body(Modifier.align(Alignment.Center), onClickUserInterfaceNavigation)
        Footer(Modifier.align(Alignment.BottomCenter), onClickSignUpScreen, onClickForgottenPasswordScreen)

    }
}

@Composable
fun Body(modifier: Modifier, onClickUserInterfaceNavigation: () -> Unit = {}) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var isLoginEnable by rememberSaveable {
        mutableStateOf(false)

    }

    Column(modifier = modifier) {
        ImageLogo(150, Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(8.dp))
        Email(email) {
            email = it
            isLoginEnable = enableLogin(email, password)

        }
        Spacer(modifier = Modifier.size(8.dp))
        Password(password) {
            password = it
            isLoginEnable = enableLogin(email, password)
        }
        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(isLoginEnable, onClickUserInterfaceNavigation)


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = email,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {

    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Contraseña") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorResource(id = R.color.textfield_color),
            focusedIndicatorColor = colorResource(id = R.color.primary_color),
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            val imagen = if (passwordVisibility) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility

            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = imagen, contentDescription = "Show password")
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }

    )

}

@Composable
fun LoginButton(loginEnable: Boolean, onClickUserInterfaceNavigation: () -> Unit = {}) {

    Button(
        onClick = onClickUserInterfaceNavigation,
        enabled = loginEnable,
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

private fun enableLogin(email: String, password: String): Boolean {

    return Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6
}

@Composable
fun Footer(
    modifier: Modifier,
    onClickSignUpScreen: () -> Unit = {},
    onClickForgottenPasswordScreen: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        ForgottenPasswordButton(onClickForgottenPasswordScreen)
        Spacer(modifier = Modifier.size(16.dp))
        SignUp(onClickSignUpScreen)
        Spacer(modifier = Modifier.size(16.dp))

    }


}

@Composable
fun ForgottenPasswordButton(onClickForgottenPasswordScreen: () -> Unit = {}) {
    TextButton(onClick = onClickForgottenPasswordScreen) {
        Text(
            text = "Olvidé mi contraseña", Modifier.padding(horizontal = 8.dp), color = colorResource(
                id = R.color.disabled_color
            )
        )
    }
}

@Composable
fun SignUp(onClickSignUpScreen: () -> Unit = {}) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "¿No tienes una cuenta?",
            Modifier.padding(horizontal = 4.dp),
            color = colorResource(
                id = R.color.disabled_color
            )
        )
        TextButton(onClick = onClickSignUpScreen) {
            Text(
                "Registrate",
                color = colorResource(id = R.color.primary_color),
                textDecoration = TextDecoration.Underline
            )

        }
    }
}