package social.ufind


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
import social.ufind.UfindApplication.Companion.setToken
import social.ufind.data.OptionsRoutes
import social.ufind.data.datastore.DataStoreManager
import social.ufind.navigation.NavigationComponent
import social.ufind.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val viewModel: MainViewModel = viewModel<MainViewModel>(factory = MainViewModel.Factory)
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val startDestination = viewModel.startDestination.collectAsStateWithLifecycle()

            LaunchedEffect(dataStoreManager.getUserData()) {
                lifecycleScope.launch {
                    val user = dataStoreManager.getUserData()
                    user.collect{
                        setToken(it.token)
                        if(it.token == "")
                            viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
                        else {
                            viewModel.validateToken()
                            if (!viewModel.isUserValid.value)
                                viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
                            else
                                viewModel.updateStartDestination(OptionsRoutes.UserInterface.route)
                        }
                    }
                }
            }
            NavigationComponent(navHostController = navController, startDestination = startDestination.value)
        }
    }
}