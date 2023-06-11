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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SettingsSecurityScreen() {
    SecurityScreen()

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



@Composable
fun CustomCardCheck(titleS:String,titleS2:String,checked2: Boolean)
{
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation=4.dp,
             shape = RoundedCornerShape(16.dp))
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
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Checkbox(
                checked = checked2,
                onCheckedChange = { isChecked -> /* handle checkbox state change */ },
                modifier = Modifier.clip(MaterialTheme.shapes.small)
            )
        }
    }
}



@Composable
fun SecurityScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column(modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White)) {


            CustomCardCheck(titleS = "cambiar contraseña", titleS2 = "contraseña",checked2 = false)

            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(16.dp))

            // Segundo componente
            CustomCardCheck(titleS = "Edad Visible ", titleS2 = "tu edad se mostrara",checked2 = true)
            Spacer(modifier = Modifier.height(16.dp))

            // Segundo componente
            CustomCardCheck(titleS = "Visibilidad del correo elecronico ", titleS2 = "tu correo se mostrara",checked2 = false)

        }
    }
}