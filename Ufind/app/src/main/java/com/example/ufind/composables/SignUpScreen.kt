package com.example.ufind.composables

import android.util.Log
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
fun SignUpScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SignUpBody(Modifier.align(Alignment.Center))
        SignUpFooter(Modifier.align(BottomCenter))
    }
}

@Composable
fun SignUpBody(modifier: Modifier) {
    var createdUserName by rememberSaveable {
        mutableStateOf("")
    }
    var newEmail by rememberSaveable {
        mutableStateOf("")

    }
    var createdPassword by rememberSaveable {
        mutableStateOf("")
    }

    var repeatedPassword by rememberSaveable {
        mutableStateOf("")
    }
    var isSignUpEnable by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = modifier) {

        ImageLogo(Modifier.align(CenterHorizontally))
        Spacer(modifier = Modifier.size(8.dp))

        CreatedUserName(createdUserName) { createdUserName = it }
        Spacer(Modifier.size(8.dp))

        NewEmail(email = newEmail, onTextChanged = {
            newEmail = it
        })
        Spacer(Modifier.size(8.dp))

        NewPassword(password = createdPassword, onTextChanged = {
            createdPassword = it
        })
        Spacer(Modifier.size(8.dp))

        RepeatPassword(repeatedPassword = repeatedPassword) {
            repeatedPassword = it
            isSignUpEnable = enableSignUp(newEmail, createdPassword, repeatedPassword)

        }
        Spacer(modifier = Modifier.size(16.dp))

        SignUpButton(signUpEnable = isSignUpEnable, createdUserName, repeatedPassword)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatedUserName(createdUserName: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = createdUserName,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Nombre de usuario") },
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
fun NewEmail(email: String, onTextChanged: (String) -> Unit) {
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
fun NewPassword(password: String, onTextChanged: (String) -> Unit) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatPassword(repeatedPassword: String, onTextChanged: (String) -> Unit) {

    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = repeatedPassword,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Confirmar contraseña") },
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
fun SignUpButton(signUpEnable: Boolean, createdUserName: String, repeatedPassword: String) {
    Button(
        onClick = { Log.i("NewSignUp", "Username $createdUserName Password: $repeatedPassword") },
        enabled = signUpEnable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White

        )
    ) {
        Text("Registrarse")

    }
}

fun enableSignUp(email: String, password: String, repeatedPassword: String): Boolean {

    return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.length > 6 && repeatedPassword == password
}

@Composable
fun SignUpFooter(modifier: Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = CenterHorizontally) {
        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        UserLogInOption()
    }

}

@Composable
fun UserLogInOption(){
    Row(horizontalArrangement = Arrangement.Center) {
        Text("¿Ya tienes una cuenta?", color = colorResource(id = R.color.disabled_color), modifier = Modifier.padding(6.dp))
        Text("Inicia Sesión", color = colorResource(id = R.color.primary_color), textDecoration = TextDecoration.Underline, modifier = Modifier.padding(6.dp))

    }
}