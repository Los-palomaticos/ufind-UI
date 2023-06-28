package social.ufind.ui.screen.settings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.ufind.R

@Preview
@Composable
fun SettingsAccountScreen(onClickSettingsScreen: () -> Unit = {}) {

    AccountScreen(onClickSettingsScreen)

}

@Composable
fun AccountScreen(onClickSettingsScreen: () -> Unit = {}) {
    var newInstitution by rememberSaveable {
        mutableStateOf("")
    }
    var newUserName by rememberSaveable {
        mutableStateOf("")
    }

    var newResidence by rememberSaveable {
        mutableStateOf("")
    }
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())

        ) {
            HeaderConfigurationCard("Configuración cuenta", onClickSettingsScreen)

            Spacer(modifier = Modifier.height(32.dp))
            // Cambiar institucion
            ChangeAccountSettingsCard(title = "Institución",
                value = newInstitution,
                placeHolderText = "UCA",
                onTextChanged = { newInstitution = it })
            Spacer(modifier = Modifier.height(32.dp))

            //  Cambiar nombre de usuario
            ChangeAccountSettingsCard(title = "Nombre de usuario",
                value = newUserName,
                placeHolderText = "Chompi",
                onTextChanged = { newUserName = it })
            Spacer(modifier = Modifier.height(32.dp))

            // Cambiar Residencia
            ChangeAccountSettingsCard(title = "Residencia",
                value = newResidence,
                placeHolderText = "San Salvador",
                onTextChanged = { newResidence = it })
            Spacer(modifier = Modifier.height(64.dp))
            SaveNewAccountSettingsButton(
                isShowDialogAvailable = {
                    // TODO("En esta lambda enviar datos de la cambios de cuenta antes de mostrar el dialogo)
                    showDialog = changeShowDialogAccountSettings(showDialog)
                })
            AreYouSureToDoThisChangesDialog(
                userNameChanged = newUserName,
                institutionChanged = newInstitution,
                residencyChanged = newResidence,
                showDialog, onDismiss = { showDialog = false },
                onClickSettingsScreen
            )

        }


        // Cambiar fecha de nacimiento -> ¿necesario?
        /*            ChangeAccountSettingsCard(
                        title = "Cumpleños",
                        title2 = "27 de noviembre",
                        onTextChanged = {})
                    Spacer(modifier = Modifier.height(12.dp))*/

    }
}

@Composable
fun SaveNewAccountSettingsButton(
    isShowDialogAvailable: () -> Unit = {}
) {

    Button(
        onClick = isShowDialogAvailable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.text_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text("Guardar cambios")

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeAccountSettingsCard(
    title: String, value: String, placeHolderText: String, onTextChanged: (String) -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ), modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp, RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title, modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
                )
                TextField(
                    value = value,
                    onValueChange = { onTextChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = placeHolderText) },
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun AreYouSureToDoThisChangesDialog(
    userNameChanged: String = "",
    institutionChanged: String = "",
    residencyChanged: String = "",
    show: Boolean = true,
    onDismiss: () -> Unit = {},
    onClickSettingsScreen: () -> Unit = {}
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
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {

                if (userNameChanged != "" || institutionChanged != "" || residencyChanged != "") {
                    Text(
                        text = "¡Sus cambios se han guardado exitosamente!",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = colorResource(
                            id = R.color.text_color
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (userNameChanged != "") {
                    Text("Nuevo nombre de usuario: $userNameChanged")
                    Spacer(modifier = Modifier.height(6.dp))

                }
                if (institutionChanged != "") {
                    Text("Nueva institución: $institutionChanged")
                    Spacer(modifier = Modifier.height(6.dp))

                }
                if (residencyChanged != "") {
                    Text("Nueva residencia: $residencyChanged")
                    Spacer(modifier = Modifier.height(6.dp))

                }
                if (userNameChanged == "" && institutionChanged == "" && residencyChanged == "") {
                    Text(
                        text = "No ha hecho ningún cambio, por favor llenar los campos requeridos",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                }
                Spacer(modifier = Modifier.size(16.dp))
                GoBackToSettingsScreenButton(onClickSettingsScreen)

            }

        }
    }
}

fun changeShowDialogAccountSettings(isShowDialogAvailable: Boolean): Boolean {
    var holderShow = isShowDialogAvailable
    holderShow = true
    return holderShow
}