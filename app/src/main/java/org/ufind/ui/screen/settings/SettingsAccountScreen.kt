package org.ufind.ui.screen.settings


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ufind.R

@Preview
@Composable
fun SettingsAccountScreen() {
    AccountScreen()

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCard3(title: String, title2: String,onTextChanged: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .height(98.dp)
            .fillMaxWidth()
            .shadow(elevation = 4.dp,
                RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 0.dp,vertical=0.dp)
                )
                TextField(value = title2,
                    onValueChange = { onTextChanged(it) },
                    modifier = Modifier

                        .wrapContentWidth()
                        .sizeIn(minWidth = 0.dp, maxWidth = 200.dp),
                    placeholder = { Text(text = "Institution") },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

@Composable
fun AccountScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column(modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White)) {


            CustomCard3(title = "Institution", title2 = "UCA", onTextChanged ={} )

            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(12.dp))

            // Segundo componente
            CustomCard3(title = "Cumpleños", title2 = "27 de noviembre", onTextChanged ={} )
            Spacer(modifier = Modifier.height(12.dp))

            // Segundo componente
            CustomCard3(title = "Nombre", title2 = "Username", onTextChanged ={} )
            Spacer(modifier = Modifier.height(12.dp))

            // Segundo componente
            CustomCard3(title = "Residencia", title2 = "San Salvador", onTextChanged ={} )
        }
    }
}