package com.example.ufind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ufind.screen.HomeMenuScreen
import com.example.ufind.screen.SignUpScreen
import com.example.ufind.ui.theme.UfindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UfindTheme {
                UserInterfaceNavigation()
            }
        }
    }
}
