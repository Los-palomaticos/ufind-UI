package org.ufind.ui.screen.home.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R
import org.ufind.ui.screen.userpost.addpost.ui.BottomBarPostIcons
import org.ufind.ui.screen.userpost.addpost.ui.PostImage


@Preview(showBackground = true)
@Composable
fun ItemPost() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical=8.dp), colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Por username01",
                color = colorResource(id = R.color.disabled_color),
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(16.dp))
            Text(
                "Lorem ipsum dolor sit amet",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.size(16.dp))
            PostImage(size = 125)
            Spacer(Modifier.size(16.dp))
            BottomBarPostIcons()

        }


    }
}