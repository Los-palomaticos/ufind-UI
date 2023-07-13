package social.ufind.ui.screen.home.user.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ufind.R
import social.ufind.navigation.OptionsRoutes
import social.ufind.navigation.NavRoute
import social.ufind.ui.screen.home.user.settings.viewmodel.SettingsViewModel

val colorbutton = Color(0xFF001233)
object SettingsScreen: NavRoute<SettingsViewModel> {
    override val route: String
        get() = OptionsRoutes.SettingsScreen.route
    @Composable
    override fun viewModel(): SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
    @Composable
    override fun Content(viewModel: SettingsViewModel) {
        SettingsScreen(viewModel)
    }

}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())

        ) {

            // Primer componente
            HeaderConfiguration {
                viewModel.navigateBack()
            }

            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(32.dp))

            // Segundo componente
            ConfigurationsButtons(viewModel)

            Spacer(modifier = Modifier.height(154.dp))
            SignOutButton{
                viewModel.logout()
            }
        }
    }
}

@Composable
fun HeaderConfiguration(onClickGoToProfileScreen: () -> Unit = {}) {
    HeaderConfigurationCardScreen(title = "Configuracion", onClickGoToProfileScreen = onClickGoToProfileScreen)
}

//Header Card
@Composable
fun HeaderConfigurationCardScreen(title: String, onClickGoToProfileScreen: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()

            .shadow(
                elevation = 3.dp,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClickGoToProfileScreen) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = colorResource(id = R.color.text_color),
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(text = title)
            }
        }
    }
}

@Composable
fun ConfigurationsButtons(
    viewModel: SettingsViewModel,
) {
    Column {
        ConfigurationButton(
            text = "Seguridad",
            icon = Icons.Default.Security,// Ejemplo de un icono predefinido de Jetpack Compose
            onClick = { viewModel.navigateToSecurity() }
        )

        Spacer(modifier = Modifier.padding(8.dp))
        ConfigurationButton(
            text = "Preferencias",
            icon = Icons.Default.TrackChanges,// Ejemplo de un icono predefinido de Jetpack Compose
            onClick = { viewModel.navigateToPreferences() }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ConfigurationButton(
            text = "Cuenta",
            icon = Icons.Default.AccountTree,// Ejemplo de un icono predefinido de Jetpack Compose
            onClick = { viewModel.navigateToAccount() }
        )
    }
}


@Composable
fun ConfigurationButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,

        modifier = Modifier
            .shadow(
                elevation = 3.dp,
                shape = MaterialTheme.shapes.medium
            )
            .height(60.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = Color.Black
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}


@Composable
fun SignOutButton(onClick: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        SignOutConfigurationButton(text = "Cerrar Sesión") { onClick() }
    }
}


@Composable
fun SignOutConfigurationButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,

        modifier = Modifier
            .shadow(
                elevation = 3.dp,
                shape = MaterialTheme.shapes.medium
            )
            .height(54.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        content = {
            Text(
                text = text,
                textAlign = TextAlign.Right,
                color = Color.Red
            )
        }
    )
}


//MENSAJE
@Composable
fun ConfirmationMessage(
    message: String,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Aceptar",
                    color = Color.Black
                )
            }
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = "Cancelar")
            }
        }
    }
}


@Preview
@Composable
fun PreviewConfirmationMessage() {
    ConfirmationMessage(
        message = "¿Deseas confirmar esta petición?",
        onAccept = { /* Acción al aceptar la petición */ },
        onCancel = { /* Acción al cancelar la petición */ },
        color = colorbutton
    )
}