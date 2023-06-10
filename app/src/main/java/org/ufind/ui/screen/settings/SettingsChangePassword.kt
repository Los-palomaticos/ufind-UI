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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
val colorbu = Color(0xFF001233)

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordCard() {
    // Estado para almacenar el valor del TextField
    val passwordState = remember { mutableStateOf("") }
    val newpasswordState = remember { mutableStateOf("") }
    val confirmpasswordState = remember { mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
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

            // Input fields for old password, new password, and confirm password
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {newValue -> passwordState.value = newValue },
                label = { Text("Contraseña actual") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newpasswordState.value,
                onValueChange = {newValue -> newpasswordState.value = newValue },
                label = { Text("Nueva contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmpasswordState.value,
                onValueChange = {newValue -> confirmpasswordState.value = newValue },
                label = { Text("Confirmar nueva contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            // Button to submit the password change

        }
    }
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
            Text(text = "Cancelar", color = Color.White,)
        }
    }
}
@Composable
fun ChangeScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column(modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White)) {

            Spacer(modifier = Modifier.height(36.dp))
            // Primer componente
            TitleChange()

            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(36.dp))

            // Segundo componente
            ChangePasswordCard()

            Spacer(modifier = Modifier.height(132.dp))
            SaveCancelButtons()
            MessageCard(message = "La contrasena se ha cambiado exitosamente")

        }
    }
}
@Preview
@Composable
fun SettingsChangePassword() {
    ChangeScreen()
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

@Composable
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
    }
}