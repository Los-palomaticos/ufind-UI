package org.ufind.ui.screen.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
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
fun SettingsPreferencesScreen(onClickSettingsScreen: () -> Unit = {}) {
    PreferencesScreen(onClickSettingsScreen)

}


@Composable
fun PreferencesScreen(onClickSettingsScreen: () -> Unit = {}) {
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
            HeaderConfigurationCard(title = "Preferencias", onClick = onClickSettingsScreen)
            // Espacio entre los componentes
            Spacer(modifier = Modifier.height(16.dp))

            // Segundo componente
            CustomCardCheckPreferences(
                titleS = "Mostrar perfil",
                titleS2 = "Tu perfil sera público"
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}


@Composable
fun CustomCardCheckPreferences(titleS: String, titleS2: String) {
    var checkBoxStateShowProfile by rememberSaveable {
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
                checked = checkBoxStateShowProfile,
                onCheckedChange = { isChecked ->
                    checkBoxStateShowProfile = !checkBoxStateShowProfile
                },
                enabled = true,
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.text_color),

                    ),
                modifier = Modifier.clip(MaterialTheme.shapes.small)
            )
        }
    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditableCardPreferences() {
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
}*/
