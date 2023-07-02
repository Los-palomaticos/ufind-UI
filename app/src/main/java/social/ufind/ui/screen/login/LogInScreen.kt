package social.ufind.ui.screen.login

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ufind.R
import social.ufind.navigation.OptionsRoutes
import social.ufind.navigation.NavRoute
import social.ufind.ui.screen.home.post.ImageLogo
import social.ufind.ui.screen.login.viewmodel.LoginViewModel

object LoginScreen : NavRoute<LoginViewModel> {
    override val route: String
        get() = OptionsRoutes.LogIn.route

    @Composable
    override fun viewModel() = viewModel<LoginViewModel>(factory = LoginViewModel.Factory)

    @Composable
    override fun Content(viewModel: LoginViewModel) {
        LoginScreen(viewModel)
    }
}

//@Preview(showBackground = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Body(viewModel = viewModel, modifier = Modifier.align(Alignment.Center))
        Footer(viewModel = viewModel, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun handleUiState(viewModel: LoginViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.value is LoginUiState.ErrorWithMessage) {
        (uiState.value as LoginUiState.ErrorWithMessage).messages.forEach { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun Body(viewModel: LoginViewModel, modifier: Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        ImageLogo(150, Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(8.dp))

        handleUiState(viewModel = viewModel)

        Email(viewModel.email.value) {
            viewModel.email.value = it
            viewModel.checkData()

        }
        Spacer(modifier = Modifier.size(8.dp))
        Password(viewModel.password.value) {
            viewModel.password.value = it
            viewModel.checkData()

        }
        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(viewModel.isValid.value) {
            viewModel.login()
        }
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

@Composable
fun Footer(
    modifier: Modifier,
    viewModel: LoginViewModel
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        ForgottenPasswordButton {
            viewModel.navigateToRoute(OptionsRoutes.ForgottenPassword.route)
        }
        Spacer(modifier = Modifier.size(16.dp))
        SignUp {
            viewModel.navigateToRoute(OptionsRoutes.SignUp.route)
        }
        Spacer(modifier = Modifier.size(16.dp))

    }
}

@Composable
fun ForgottenPasswordButton(onClickForgottenPasswordScreen: () -> Unit = {}) {
    TextButton(onClick = onClickForgottenPasswordScreen) {
        Text(
            text = "Olvidé mi contraseña",
            Modifier.padding(horizontal = 8.dp),
            color = colorResource(
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