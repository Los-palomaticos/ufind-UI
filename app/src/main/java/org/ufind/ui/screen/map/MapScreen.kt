package org.ufind.ui.screen.map


import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.ufind.R
import org.ufind.ui.screen.map.utils.LocationUtils
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.android.gms.location.*
import com.google.maps.android.compose.*


@Preview(showBackground = true)
@Composable
fun MapScreen(onClickGoToAddPost: () -> Unit = {}) {

    val context = LocalContext.current
    var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MapScreenHeader(onClickGoToAddPost)
            Spacer(modifier = Modifier.size(16.dp))
            MapScreen(fusedLocationProviderClient)
            Spacer(modifier = Modifier.size(32.dp))
            LocationOptions()
            Spacer(modifier = Modifier.size(32.dp))

        }

    }
}



@Composable
fun MapScreenHeader(onClickGoToAddPost: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.textfield_color)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClickGoToAddPost) {
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
                Text(text = "Regresar a crear publicación")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        LocalContext.current
    )
) {

    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }
    val ucaLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }
    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LocationUtils.getPosition(currentLocation), 18f
    )

    var requestLocationUpdate by remember { mutableStateOf(true) }
    var requestLocationUpdateToUca by remember { mutableStateOf(false) }

    Column() {
        Card(
            onClick = { requestLocationUpdateToUca = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.mapbutton),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Navegar hacia UCA",
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.text_color),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        GoogleMapUfind(
            currentLocation,
            cameraPositionState,
            onGpsIconClick = {
                requestLocationUpdate = true
            }
        )
    }

    if (requestLocationUpdateToUca) {
        LocationPermissionsAndSettingDialogs(
            updateCurrentLocation = {
                requestLocationUpdateToUca = false
                LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->
                    locationResult.lastLocation?.let { location ->
                        currentLocation = ucaLocation
                    }
                }
            }
        )
    }

    if (requestLocationUpdate) {
        LocationPermissionsAndSettingDialogs(
            updateCurrentLocation = {
                requestLocationUpdate = false
                LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->
                    locationResult.lastLocation?.let { location ->
                        currentLocation = location
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationOptions() {
    var isExpandedMyA by rememberSaveable {
        mutableStateOf(false)
    }
    var isExpandedEd by rememberSaveable {
        mutableStateOf(false)
    }
    var isExpandedZc by rememberSaveable {
        mutableStateOf(false)
    }

    var isExpandedAdmin by rememberSaveable {
        mutableStateOf(false)
    }
    var locationAulasOrMagna by rememberSaveable {
        mutableStateOf("")
    }
    var locationEdificios by rememberSaveable {
        mutableStateOf("")
    }
    var locationEdificiosAdmin by rememberSaveable {
        mutableStateOf("")
    }
    var locationCommonZone by rememberSaveable {
        mutableStateOf("")
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {

        Text(text = "Magnas y Aulas")
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = isExpandedMyA,
            onExpandedChange = { isExpandedMyA = it }) {
            TextField(
                value = locationAulasOrMagna,
                onValueChange = { locationAulasOrMagna = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedMyA)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    trailingIconColor = colorResource(id = R.color.primary_color),
                    backgroundColor = colorResource(id = R.color.textfield_color),
                    focusedIndicatorColor = colorResource(id = R.color.primary_color),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = isExpandedMyA,
                onDismissRequest = { isExpandedMyA = false }) {
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Magna I"
                    isExpandedMyA = false
                }) {
                    Text(text = "Magna I")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Magna II"
                    isExpandedMyA = false
                }) {
                    Text(text = "Magna II")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Magna III"
                    isExpandedMyA = false
                }) {
                    Text(text = "Magna III")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Magna IV"
                    isExpandedMyA = false
                }) {
                    Text(text = "Magna IV")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Magna V"
                    isExpandedMyA = false
                }) {
                    Text(text = "Magna V")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Magna VI"
                    isExpandedMyA = false
                }) {
                    Text(text = "Magna VI")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Aulas A"
                    isExpandedMyA = false
                }) {
                    Text(text = "Aulas A")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Aulas B"
                    isExpandedMyA = false
                }) {
                    Text(text = "Aulas B")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Aulas C"
                    isExpandedMyA = false
                }) {
                    Text(text = "Aulas C")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = "Aulas D"
                    isExpandedMyA = false
                }) {
                    Text(text = "Aulas D")
                }
                DropdownMenuItem(onClick = {
                    locationAulasOrMagna = ""
                    isExpandedMyA = false
                }) {
                    Text(text = "---")
                }
            }
        }

        // edificios
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Edificios")
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = isExpandedEd,
            onExpandedChange = { isExpandedEd = it }) {
            TextField(
                value = locationEdificios,
                onValueChange = { locationEdificios = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedEd)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    trailingIconColor = colorResource(id = R.color.primary_color),
                    backgroundColor = colorResource(id = R.color.textfield_color),
                    focusedIndicatorColor = colorResource(id = R.color.primary_color),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = isExpandedEd,
                onDismissRequest = { isExpandedEd = false }) {
                DropdownMenuItem(onClick = {
                    locationEdificios = "Cindae"
                    isExpandedEd = false
                }) {
                    Text(text = "Cindae")
                }
                DropdownMenuItem(onClick = {
                    locationEdificios = "ICAS"
                    isExpandedEd = false
                }) {
                    Text(text = "ICAS")
                }
                DropdownMenuItem(onClick = {
                    locationEdificios = "Jon de Cortina, S.J"
                    isExpandedEd = false
                }) {
                    Text(text = "Jon de Cortina, S.J")
                }

                DropdownMenuItem(onClick = {
                    locationEdificios = "Francisco Andrés Escobar"
                    isExpandedEd = false
                }) {
                    Text(text = "Francisco Andrés Escobar")
                }
                DropdownMenuItem(onClick = {
                    locationEdificios = "P. Ignacio Martín-Baró, S.J"
                    isExpandedEd = false
                }) {
                    Text(text = "P. Ignacio Martín-Baró, S.J")
                }
                DropdownMenuItem(onClick = {
                    locationEdificios = "Taller de Simulación Bernardo Pohl"
                    isExpandedEd = false
                }) {
                    Text(text = "Taller de Simulación Bernardo Pohl")
                }
                DropdownMenuItem(onClick = {
                    locationEdificios = "Laboratorios de Ingeniería"
                    isExpandedEd = false
                }) {
                    Text(text = "Laboratorios de Ingeniería")
                }

                DropdownMenuItem(onClick = {
                    locationEdificios = "Laboratorios de Psicología"
                    isExpandedEd = false
                }) {
                    Text(text = "Laboratorios de Psicología")
                }
                DropdownMenuItem(onClick = {
                    locationEdificios = "Laboratorio de Estructuras Grandes"
                    isExpandedEd = false
                }) {
                    Text(text = "Laboratorio de Estructuras Grandes")
                }
                DropdownMenuItem(onClick = {
                    locationEdificios = ""
                    isExpandedEd = false
                }) {
                    Text(text = "---")
                }
            }
        }

        // edificios administrativos
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Edificios Administrativos")
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = isExpandedAdmin,
            onExpandedChange = { isExpandedAdmin = it }) {
            TextField(
                value = locationEdificiosAdmin,
                onValueChange = { locationEdificios = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedAdmin)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.textfield_color),
                    focusedIndicatorColor = colorResource(id = R.color.primary_color),
                    trailingIconColor = colorResource(id = R.color.primary_color),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = isExpandedAdmin,
                onDismissRequest = { isExpandedAdmin = false }) {
                DropdownMenuItem(onClick = {
                    locationEdificiosAdmin = "Direcciones de Redes de Información y Sistemas"
                    isExpandedEd = false
                }) {
                    Text(text = "Direcciones de Redes de Información y Sistemas..")
                }
                DropdownMenuItem(onClick = {
                    locationEdificiosAdmin = "Edificio Administrativo Anexo"
                    isExpandedAdmin = false
                }) {
                    Text(text = "Edificio Administrativo Anexo")
                }
                DropdownMenuItem(onClick = {
                    locationEdificiosAdmin = "Edificio de Administración Central"
                    isExpandedAdmin = false
                }) {
                    Text(text = "Edificio de Administración Central")
                }

                DropdownMenuItem(onClick = {
                    locationEdificiosAdmin = "Edificio de Rectoría"
                    isExpandedAdmin = false
                }) {
                    Text(text = "Edificio de Rectoría")
                }
                DropdownMenuItem(onClick = {
                    locationEdificiosAdmin = "Vicerrectoría Financiera"
                    isExpandedAdmin = false
                }) {
                    Text(text = "Vicerrectoría Financiera")
                }
                DropdownMenuItem(onClick = {
                    locationEdificiosAdmin = ""
                    isExpandedAdmin = false
                }) {
                    Text(text = "---")
                }
            }
        }
        // Zonas Comunes
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Zonas comúnes")
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = isExpandedZc,
            onExpandedChange = { isExpandedZc = it }) {
            TextField(
                value = locationCommonZone,
                onValueChange = { locationCommonZone = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedZc)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    trailingIconColor = colorResource(id = R.color.primary_color),
                    backgroundColor = colorResource(id = R.color.textfield_color),
                    focusedIndicatorColor = colorResource(id = R.color.primary_color),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = isExpandedZc,
                onDismissRequest = { isExpandedZc = false }) {
                DropdownMenuItem(onClick = {
                    locationCommonZone = "Cafetería Central"
                    isExpandedZc = false
                }) {
                    Text(text = "Cafetería Central")
                }
                DropdownMenuItem(onClick = {
                    locationCommonZone = "Cafetería Peatonal"
                    isExpandedZc = false
                }) {
                    Text(text = "Cafetería Peatonal")
                }
                DropdownMenuItem(onClick = {
                    locationCommonZone = "Biblioteca Central"
                    isExpandedZc = false
                }) {
                    Text(text = "Biblioteca Central")
                }

                DropdownMenuItem(onClick = {
                    locationCommonZone = "Centro Polideportivo"
                    isExpandedZc = false
                }) {
                    Text(text = "Centro Polideportivo")
                }
                DropdownMenuItem(onClick = {
                    locationCommonZone = "Parroquia Jesucristo Liberador"
                    isExpandedZc = false
                }) {
                    Text(text = "Parroquia Jesucristo Liberador")
                }
                DropdownMenuItem(onClick = {
                    locationCommonZone = ""
                    isExpandedZc = false
                }) {
                    Text(text = "---")
                }
            }
        }
        Spacer(modifier = Modifier.size(28.dp))
        ButtonSaveLocation(locationEdificios, locationEdificiosAdmin, locationCommonZone, locationAulasOrMagna)
    }
}

@Composable
fun ButtonSaveLocation(
    locationEdificios: String,
    locationEdificiosAdmin: String,
    locationCommonZone: String,
    locationAulasOrMagna: String
) {
    Button(
        onClick = {
                  //TODO("DEPENDIENDO DE QUE CAMPO NO SEA "" UNA STRING VACIA ENTONCES SE MANDARÁ COMO INFORMACIÓN PARA LA UBICACIÓN DEL POST")
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.text_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text("Guardar ubicación")
    }
}

@Composable
private fun GoogleMapUfind(
    currentLocation: Location,
    cameraPositionState: CameraPositionState,
    onGpsIconClick: () -> Unit
) {
    // on below line creating a variable for location.
    val locationName = remember {
        mutableStateOf("")
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
        )
    }
    val properties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true))
    }
    val ucaLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    GoogleMap(
        modifier = Modifier.height(450.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = properties
    ) {

        Marker(
            state = MarkerState(position = LocationUtils.getPosition(ucaLocation)),
            title = "UCA"
        )

    }

    //GpsIconButton(onIconClick = onGpsIconClick)
    //DebugOverlay(cameraPositionState)
}

@Composable
private fun GpsIconButton(onIconClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(onClick = onIconClick) {
                Icon(
                    modifier = Modifier.padding(bottom = 100.dp, end = 20.dp),
                    painter = painterResource(id = R.drawable.ic_gps_fixed),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun DebugOverlay(
    cameraPositionState: CameraPositionState,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        val moving =
            if (cameraPositionState.isMoving) "moving" else "not moving"
        Text(
            text = "Camera is $moving",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Text(
            text = "Camera position is ${cameraPositionState.position}",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}


