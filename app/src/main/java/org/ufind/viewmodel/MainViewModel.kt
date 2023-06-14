package org.ufind.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel(): ViewModel() {
    val isLogged = mutableStateOf(false)

}