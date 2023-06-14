package org.ufind.ui.screen.signup

import android.util.Log
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import org.ufind.R
import org.ufind.data.OptionsRoutes
import org.ufind.navigation.NavRoute
import org.ufind.ui.screen.signup.viewmodel.SignUpViewModel
import org.ufind.ui.screen.userhomescreen.ImageLogo

object SignUpScreen: NavRoute<SignUpViewModel> {
    override val route: String
        get() = OptionsRoutes.SignUp.route
    @Composable
    override fun viewModel(): SignUpViewModel = viewModel<SignUpViewModel>(factory = SignUpViewModel.Factory)
    @Composable
    override fun Content(viewModel: SignUpViewModel) {
        SignUpScreen(viewModel)
    }

}

//@Preview(showBackground = true)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel
) {
    viewModel.handleUiStatus(LocalContext.current)
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SignUpBody(modifier = Modifier.align(Alignment.Center), viewModel = viewModel)
        SignUpFooter(modifier = Modifier.align(BottomCenter))
    }
}

@Composable
fun SignUpBody(modifier: Modifier, viewModel: SignUpViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    Column(modifier = modifier) {

        ImageLogo(150, Modifier.align(CenterHorizontally))
        Spacer(modifier = Modifier.size(8.dp))
        if(uiState.value is SignUpUiState.ErrorWithMessage){
            Text(
                text = (uiState.value as SignUpUiState.ErrorWithMessage).message,
                color = MaterialTheme.colorScheme.error
            )
        }
        CreatedUserName(viewModel.username.value) {
            viewModel.username.value = it
            viewModel.checkData()
        }
        Spacer(Modifier.size(8.dp))

        NewEmail(email = viewModel.email.value, onTextChanged = {
            viewModel.email.value = it
            viewModel.checkData()
        })
        Spacer(Modifier.size(8.dp))

        NewPassword(password = viewModel.password.value, onTextChanged = {
            viewModel.password.value = it
            viewModel.checkData()
        })
        Spacer(Modifier.size(8.dp))

        RepeatPassword(repeatedPassword = viewModel.repeatedPassword.value) {
            viewModel.repeatedPassword.value = it
            viewModel.checkData()
        }
        Spacer(modifier = Modifier.size(16.dp))

        SignUpButton(signUpEnable = viewModel.isValid.value) {
            viewModel.signup()
        }
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
fun SignUpButton(
    signUpEnable: Boolean,
    onClick: () -> Unit ={}
) {
    Button(
        onClick = onClick,
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
        UserLogInOption(/* TODO */)
    }

}

@Composable
fun UserLogInOption(onClickLogInScreen: () -> Unit={}) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "¿Ya tienes una cuenta?",
            color = colorResource(id = R.color.disabled_color),
            modifier = Modifier.padding(4.dp)
        )
        TextButton(onClick = onClickLogInScreen) {
            Text(
                "Inicia sesión",
                color = colorResource(id = R.color.primary_color),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}