package social.ufind.ui.screen.home.user.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R

@Preview
@Composable
fun SettingsSecurityScreen(
    onClickSettingsScreen: () -> Unit = {},
    onClickSettingsChangePassword: () -> Unit = {}
) {
    SecurityScreen(onClickSettingsScreen, onClickSettingsChangePassword)
}


@Composable
fun SecurityScreen(
    onClickSettingsScreen: () -> Unit = {},
    onClickSettingsChangePassword: () -> Unit = {}
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            HeaderConfigurationCard(title = "Seguridad", onClick = onClickSettingsScreen)
            Spacer(modifier = Modifier.size(32.dp))


            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(16.dp))

            // Segundo componente
            CustomCardCheck(
                titleS = "Edad visible ",
                titleS2 = "Tu edad se mostrará al público"
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Segundo componente
            CustomCardCheck(
                titleS = "Visibilidad del correo elecronico ",
                titleS2 = "Tu correo se mostrará al público")
            Spacer(modifier = Modifier.height(16.dp))

            // Segundo componente
            CustomCardCheck(
                titleS = "Residencia visible ",
                titleS2 = "Tu residencia será visible al público")
            Spacer(modifier = Modifier.height(16.dp))
            ConfigurationButton(
                text = "Cambiar contraseña",
                icon = Icons.Default.ArrowRight,
                onClick = onClickSettingsChangePassword
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HeaderConfigurationCard(
    title: String,
    onClick: () -> Unit
) {
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
            IconButton(onClick = onClick) {
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
fun CustomCardCheck(titleS: String, titleS2: String) {
    var cardCheckState by rememberSaveable {
        mutableStateOf(false)

    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titleS,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = titleS2,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Checkbox(
                checked = cardCheckState,
                onCheckedChange = { cardCheckState = !cardCheckState},
                enabled = true,
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.text_color),

                ),
                modifier = Modifier.clip(MaterialTheme.shapes.small)
            )
        }
    }
}

fun changeCheckBoxState(state: Boolean): Boolean {
    return !state
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditableCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Editar",
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.Gray
                )
            }
            // Input field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Texto editable") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
