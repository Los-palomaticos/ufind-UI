package org.ufind.ui.screen.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.ufind.R


@Composable
fun MapScreen(onClickGoToAddPost: () -> Unit = {}) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Column{
            MapScreenHeader(onClickGoToAddPost)
            Spacer(modifier = Modifier.size(16.dp))
            MapScreenBody()
            
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
                Text(text = "Regresar a crear post")
            }
        }
    }
}

@Composable
fun MapScreenBody(){

    val uca = LatLng(13.68119828816205, -89.23588588775722)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(uca, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        Marker(
            state = MarkerState(position = uca),
            title = "UCA",
            snippet = "Marcador en UCA"
        )
    }
}