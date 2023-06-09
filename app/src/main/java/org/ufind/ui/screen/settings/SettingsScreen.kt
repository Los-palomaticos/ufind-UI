package org.ufind.ui.screen.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ufind.R

@Preview
@Composable
fun ConfigurationsScreen() {
    Column() {
        ConfigurationsButton(
            text = "Seguridad",
            icon = Icons.Default.ArrowRight,// Ejemplo de un icono predefinido de Jetpack Compose
            onClick = { /* Acción al hacer clic en el botón */ }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ConfigurationsButton(
            text = "Preferencias",
            icon = Icons.Default.ArrowRight,// Ejemplo de un icono predefinido de Jetpack Compose
            onClick = { /* Acción al hacer clic en el botón */ }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ConfigurationsButton(
            text = "Cuenta",
            icon = Icons.Default.ArrowRight,// Ejemplo de un icono predefinido de Jetpack Compose
            onClick = { /* Acción al hacer clic en el botón */ }
        )
    }
}
@Composable
fun ConfigurationsButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
   
        modifier = Modifier
            .shadow(elevation = 3.dp,
                shape = MaterialTheme.shapes.medium)
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
fun BlankView() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column(modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White)) {

            // Primer componente
            PreviewCardComponent()

            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(16.dp))

            // Segundo componente
            ConfigurationsScreen()

            Spacer(modifier = Modifier.height(154.dp))
            ConfigurationsScreen2()
        }
    }
}
@Composable
fun ConfigurationsScreen2() {
    Column(Modifier.padding(16.dp)) {
        ConfigurationsButton2(text = "Cerrar Sesión") { /* Acción al hacer clic en Cerrar Sesión */ }
    }
}

@Composable
fun ConfigurationsButton2(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,

        modifier = Modifier
            .shadow(elevation = 3.dp,
                shape = MaterialTheme.shapes.medium )
            .height(54.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        content = {
            Text(text = text,
                textAlign = TextAlign.Right,
                color = Color.Red)
        }
    )
}
@Preview
@Composable
fun SettingsScreen() {
    BlankView()
}
//CARD COMPONENTE
@Composable
fun CardComponent(title: String, description: String) {
    Card(

        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.coca), // Reemplaza con tu icono de editar
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(text = title)
            }
        }
    }
}


@Composable
fun PreviewCardComponent() {
    CardComponent(title = "Configuracion", description = "Descripción de la tarjeta")
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
        Text(text = message,
            color = Color.Black)
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
                Text(text = "Aceptar",
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
        color = Color.Blue
    )
}