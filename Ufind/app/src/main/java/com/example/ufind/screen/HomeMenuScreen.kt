package com.example.ufind.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.elevatedButtonElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ufind.R
import com.example.ufind.data.BottomBarScreen
import com.example.ufind.data.BottomNavGraph


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeMenuScreen() {

    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        HomeBody()
    }
}

@Composable
fun HomeBody() {
    Column {
        PageHeader()
        FoundSomethingButton()
        Spacer(modifier = Modifier.size(32.dp))
        PostsUFind()
    }

}

@Composable
fun PostsUFind() {
    Text(text = "Publicaciones", fontSize = 16.sp)

}

@Composable
fun FoundSomethingButton() {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.text_color),
            disabledContainerColor = colorResource(id = R.color.disabled_color),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), elevation = elevatedButtonElevation(
            defaultElevation = 40.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, bottom = 0.dp, top = 32.dp)
    ) {
        Text(
            "¿Encontraste Algo?",
            color = colorResource(id = R.color.white),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun PageHeader() {
    ImageLogo(75, modifier = Modifier)
    PageHeaderLineDivider()
}

@Composable
fun PageHeaderLineDivider() {
    Divider(
        Modifier
            .height(1.dp)
            .fillMaxWidth(),
        color = Color(0xFF02092E)
    )
}






