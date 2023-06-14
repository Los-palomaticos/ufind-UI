package org.ufind


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ufind.data.OptionsRoutes
import org.ufind.data.datastore.DataStoreManager
import org.ufind.data.model.UserModel
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
                        Log.d("APP_TAG", it.toString())
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
