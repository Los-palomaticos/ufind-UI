package org.ufind.ui.screen.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.ufind.ui.screen.changepassword.BackToLogInButton
import org.ufind.ui.screen.changepassword.changeShowDialog
import org.ufind.ui.screen.changepassword.enableChangePassword

val colorbu = Color(0xFF001233)


@Preview
@Composable
fun SettingsChangePassword(onClickSettingsSecurityScreen: () -> Unit = {}) {
    ChangeScreen(onClickSettingsSecurityScreen)
}

@Preview
@Composable
fun ChangeScreen(onClickSettingsSecurityScreen: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.White)
        ) {
            HeaderConfigurationCard(
                title = "Cambiar contraseña",
                onClick = onClickSettingsSecurityScreen
            )

            Spacer(modifier = Modifier.height(36.dp))
            // Primer componente
            TitleChange()
            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(36.dp))
            // Segundo componente
            ChangePasswordCard()
            Spacer(modifier = Modifier.height(132.dp))
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordCard() {
    // Estado para almacenar el valor del TextField
    var actualPassword by rememberSaveable { mutableStateOf("") }
    var newPasswordState by rememberSaveable { mutableStateOf("") }
    var confirmPasswordState by rememberSaveable { mutableStateOf("") }
    var isChangePasswordAvailable by rememberSaveable {
        mutableStateOf(false)
    }
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Text(
                text = "Cambiar contraseña",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.size(16.dp))

            ActualPasswordSecuritySettings(
                actualpassword = actualPassword,
                onTextChanged = { actualPassword = it })
            Spacer(modifier = Modifier.size(16.dp))

            NewPasswordSecuritySettings(
                newPassword = newPasswordState,
                onTextChanged = { newPasswordState = it })

            Spacer(modifier = Modifier.size(16.dp))

            RepeatedNewPasswordSecuritySettings(
                repeatedNewPassword = confirmPasswordState,
                onTextChanged = {
                    confirmPasswordState = it
                    isChangePasswordAvailable =
                        enableChangePasswordSetting(
                            actualPassword,
                            newPasswordState,
                            confirmPasswordState
                        )
                }
            )

            Spacer(modifier = Modifier.size(32.dp))

            ChangePasswordSettingButton(
                isChangePasswordAvailable,
                confirmPasswordState,
                isShowDialogAvailable = { changeShowDialogChangePasswordSettings(showDialog) }
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualPasswordSecuritySettings(
    actualpassword: String,
    onTextChanged: (String) -> Unit
) {
    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = actualpassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Contraseña Actual") },
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordSecuritySettings(
    newPassword: String,
    onTextChanged: (String) -> Unit
) {
    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = newPassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Nueva contraseña") },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatedNewPasswordSecuritySettings(
    repeatedNewPassword: String,
    onTextChanged: (String) -> Unit
) {
    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = repeatedNewPassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Repetir nueva contraseña") },
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

@Preview
@Composable
fun SaveCancelButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = { /* Acción al guardar */ },
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(text = "Guardar", color = Color.Black)
        }

        Button(
            onClick = { /* Acción al cancelar */ },
            colors = ButtonDefaults.buttonColors(containerColor = colorbu),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)

        ) {
            Text(text = "Cancelar", color = Color.White)
        }
    }
}

@Preview
@Composable
fun TitleChange() {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("La contraseña debe tener al menos seis caracteres ")
            }
            append("e incluir una combinación de números, letras y caracteres especiales (!$@%%)")
        },
        color = Color.Black, // Color del texto
        fontSize = 16.sp, // Tamaño de la fuente
        textAlign = TextAlign.Center, // Alineación del texto
        modifier = Modifier.fillMaxWidth() // Opcional: para ocupar el ancho completo del contenedor
    )
}

/*@Composable
fun MessageCard(message: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(elevation = 4.dp,
                RoundedCornerShape(16.dp)
    )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = message
            )

        }
    }*/
@Composable
fun ChangePasswordSettingButton(
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

@Composable
fun DialogSettingsPasswordChangedCorrectly(
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

            }

        }
    }
}

fun isRepeatedPasswordOk(newChangedPassword: String, repeatedChangedPassword: String): Boolean {
    return newChangedPassword == repeatedChangedPassword
}

fun enableChangePasswordSetting(
    currentPassword: String,
    newChangedPassword: String,
    repeatedChangedPassword: String
): Boolean {
    if (currentPassword != "" && newChangedPassword.length > 6 && newChangedPassword == repeatedChangedPassword) {
        return true
    }
    return false
}

fun changeShowDialogChangePasswordSettings(isShowDialogAvailable: Boolean): Boolean {
    var holderShow = isShowDialogAvailable
    holderShow = true
    return holderShow
}