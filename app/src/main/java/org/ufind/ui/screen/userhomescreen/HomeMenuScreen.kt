package org.ufind.ui.screen.userhomescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.elevatedButtonElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R
import org.ufind.ui.screen.userpost.addpost.ui.PostScreen


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeMenuScreen(onClickAddPostScreen: () -> Unit={}) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HomeBody(onClickAddPostScreen)


    }
}

@Composable
fun HomeBody(onClickAddPostScreen: () -> Unit={}) {
    Column {
        PageHeader()
        Spacer(modifier = Modifier.size(32.dp))
        Text(text = "Publicaciones", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        PostScreen(onClickAddPostScreen)

    }
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

@Composable
fun ImageLogo(size: Int, modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_ufind),
        contentDescription = "Logo",
        modifier = modifier.size(size.dp),
        contentScale = ContentScale.Fit
    )
}

