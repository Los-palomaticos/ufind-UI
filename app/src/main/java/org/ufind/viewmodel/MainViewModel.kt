package org.ufind.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.ufind.data.OptionsRoutes
import org.ufind.data.datastore.DataStoreManager

class MainViewModel(): ViewModel() {
    var startDestination = MutableStateFlow(OptionsRoutes.LogInOrSignUpOptions.route)

    fun updateStartDestination(value: String) {
        startDestination.value = value
    }
}
