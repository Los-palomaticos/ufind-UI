package social.ufind.ui.screen.home.user.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ufind.R

@Preview(showBackground = true)
@Composable
fun WalletProfileScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        BodyWallet(Modifier.align(Alignment.Center))


    }
}


@Composable
fun BodyWallet(modifier: Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "10000 Ucoins",
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 25.sp,
            color = colorResource(id = R.color.text_color),
            fontWeight = FontWeight.Bold

        )
        Text(
            text = "Nombre",
            modifier = Modifier.padding(bottom = 8.dp),
        )



        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.text_color),
                disabledContainerColor = colorResource(id = R.color.disabled_color),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ), elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 40.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp, bottom = 0.dp,)

        ) {
            androidx.compose.material3.Text(
                "Recargar Billetera",
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(5.dp)
            )



        }






    }

}