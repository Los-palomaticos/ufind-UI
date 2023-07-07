package social.ufind.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ufind.R
@Preview
@Composable
fun ProfileGoToButtons(
    modifier: Modifier = Modifier,
    onClickProfileSettings: () -> Unit = {},
    onClickWalletButton:  () -> Unit = {}
) {
    Column(modifier = modifier) {
        GoToSettingsProfileButton(onClickProfileSettings)
        Spacer(modifier = Modifier.size(8.dp))
        GoToWalletButton(onClickWalletButton)

    }
}

@Composable
fun GoToWalletButton(onClickWalletButton: () -> Unit = {}) {
    IconButton(onClick = onClickWalletButton) {
        Icon(
            Icons.Filled.Wallet,
            contentDescription = "",
            tint = colorResource(id = R.color.text_color),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun GoToSettingsProfileButton(onClickProfileSettings: () -> Unit = {}) {
    IconButton(onClick = { onClickProfileSettings() }) {
        androidx.compose.material3.Icon(
            Icons.Filled.Settings,
            contentDescription = "",
            tint = colorResource(id = R.color.text_color),
            modifier = Modifier.size(32.dp)
        )

    }
}