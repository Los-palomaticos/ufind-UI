package org.ufind


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.ufind.UfindApplication.Companion.setToken
import org.ufind.data.OptionsRoutes
import org.ufind.data.datastore.DataStoreManager
import org.ufind.navigation.NavigationComponent
import org.ufind.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val viewModel: MainViewModel = viewModel<MainViewModel>()
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val startDestination = viewModel.startDestination.collectAsStateWithLifecycle()

            LaunchedEffect(dataStoreManager.getUserData()) {
                lifecycleScope.launch {
                    val user = dataStoreManager.getUserData()
                    user.collect{
                        setToken(it.token)
                        if(it.token != "")
                            viewModel.updateStartDestination(OptionsRoutes.UserInterface.route)
                        else
                            viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
                    }
                }
            }
            NavigationComponent(navHostController = navController, startDestination = startDestination.value)
        }
    }
}
