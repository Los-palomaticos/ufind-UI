package com.example.ufind.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ufind.R

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreen(onClickSignInScreen: () -> Unit = {}) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderForgottenPassword(modifier = Modifier.align(Alignment.TopCenter))
        ChangePasswordBody(Modifier.align(Alignment.Center), onClickSignInScreen)

    }

}

@Composable
fun ChangePasswordBody(modifier: Modifier, onClickSignInScreen: () -> Unit = {}) {
    var newChangedPassword by rememberSaveable {
        mutableStateOf("")

    }
    var repeatedChangedPassword by rememberSaveable {
        mutableStateOf("")

    }
    var isChangePasswordAvailable by rememberSaveable {
        mutableStateOf(false)

    }
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.size(16.dp))
        NewChangedPassword(newChangedPassword) {
            newChangedPassword = it
        }
        Spacer(modifier = Modifier.size(16.dp))
        RepeatChangedPassword(repeatedChangedPassword) {
            repeatedChangedPassword = it
            isChangePasswordAvailable =
                enableChangePassword(newChangedPassword, repeatedChangedPassword)
        }
        Spacer(modifier = Modifier.size(16.dp))
        ChangePasswordButton(isChangePasswordAvailable, repeatedChangedPassword) {
            showDialog = changeShowDialog(showDialog)
        }
        DialogPasswordChangedCorrectly(
            show = showDialog,
            onDismiss = { showDialog = false },
            onClickSignInScreen
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatChangedPassword(repeatedChangedPassword: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = repeatedChangedPassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Repetir contraseña") },
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
fun NewChangedPassword(newChangedPassword: String, onTextChanged: (String) -> Unit) {

    TextField(
        value = newChangedPassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Nueva contraseña") },
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
fun ChangePasswordButton(
    changePasswordAvailable: Boolean,
    repeatedChangedPassword: String,
    isShowDialogAvailable: () -> Unit = {}
) {
    Button(
        onClick = isShowDialogAvailable,
        enabled = changePasswordAvailable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White

        )
    ) {
        Text("Cambiar contraseña")
    }
}


fun enableChangePassword(newChangedPassword: String, repeatedChangedPassword: String): Boolean {
    return newChangedPassword.length > 6 && newChangedPassword == repeatedChangedPassword
}

@Composable
fun DialogPasswordChangedCorrectly(
    show: Boolean,
    onDismiss: () -> Unit,
    onClickSignInScreen: () -> Unit = {}
) {
    if (show) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(36.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Su contraseña ha sido cambiada exitosamente",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = colorResource(
                        id = R.color.text_color
                    )
                )
                Spacer(modifier = Modifier.size(16.dp))
                BackToLogInButton(onClickSignInScreen)

            }

        }


    }
}

@Composable
fun BackToLogInButton(onClickSignInScreen: () -> Unit = {}) {
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
        Text("Regresar a Iniciar sesión")
    }
}

fun changeShowDialog(isShowDialogAvailable: Boolean): Boolean {
    var holderShow = isShowDialogAvailable
    holderShow = true
    return holderShow
}